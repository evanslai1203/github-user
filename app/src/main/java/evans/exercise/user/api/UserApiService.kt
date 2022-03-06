package evans.exercise.user.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.SocketTimeoutException

/**
 * Created by evans.lai on 2022/3/5.
 */

private const val BASE_URL = "https://api.github.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(ConnectivityInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {
    @GET("users")
    fun userListAsync(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Call<List<UserResponse>>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") login: String): Call<UserDetailResponse>
}

object UserApi {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}

class ConnectivityInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return try {
            chain.proceed(builder.build())
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            throw NoConnectivityException()
        }
    }

}

// TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
fun isInternetAvailable(): Boolean {
    return try {
        val sock = Socket()
        val sockAddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
        sock.connect(sockAddr, 1000) // This will block no more than timeoutMs
        sock.close()
        true
    } catch (e: IOException) {
        false
    }
}

class NoConnectivityException : IOException()