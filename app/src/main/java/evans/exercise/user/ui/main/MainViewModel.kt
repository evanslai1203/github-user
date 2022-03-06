package evans.exercise.user.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import evans.exercise.user.api.UserApi
import evans.exercise.user.data.User
import evans.exercise.user.data.asModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by evans.lai on 2022/3/5
 */
class MainViewModel : ViewModel() {

    var userList = MutableLiveData<List<User>>(emptyList())
    val photoEvent = MutableLiveData<Pair<Int, Bitmap?>>()
    var selectedUser = MutableLiveData<User>()

    /**
     * 取得User List
     */
    suspend fun getUserList() {
        val response = UserApi.retrofitService.userListAsync(0, 100).awaitResponse()
        val userListResponse = response.body()
        val userList = userListResponse?.asModel()
        this.userList.value = userList ?: emptyList()

        // Get User Photo for each user.
        userList?.forEachIndexed { index, user ->
            viewModelScope.launch {
                val photo = withContext(Dispatchers.IO) {
                    getBitmapFromURL(user.avatarUrl)
                }
                photoEvent.value = Pair(index, photo)
            }
        }
    }

    /**
     * 讀取網路圖片，型態為Bitmap
     */
    private fun getBitmapFromURL(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun onUserClicked(user: User) {
        selectedUser.value = user
    }

    suspend fun getUserDetail() {
        val selectedUser = selectedUser.value
        val login = selectedUser?.login ?: return
        val response = UserApi.retrofitService.getUserDetail(login).awaitResponse()
        val userDetailResponse = response.body()
        if (userDetailResponse != null) {
            this.selectedUser.value = userDetailResponse.asModel().apply {
                photo = selectedUser.photo
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MainViewModel() as T)
}