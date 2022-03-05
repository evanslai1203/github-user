package evans.exercise.user.data

import android.graphics.Bitmap
import evans.exercise.user.api.UserDetailResponse
import evans.exercise.user.api.UserResponse

/**
 * Created by evans.lai on 2022/3/5.
 */
data class User(
    val login: String,
    val avatarUrl: String,
    val siteAdmin: Boolean
) {
    var photo: Bitmap? = null
    var name: String? = null
    var bio: String? = null
    var location: String? = null
    var blog: String? = null
}


fun List<UserResponse>.asModel(): List<User> {
    return map {
        User(it.login, it.avatarUrl, it.siteAdmin)
    }
}

fun UserDetailResponse.asModel(): User {
    return User(login, avatarUrl, siteAdmin).apply {
        name = this@asModel.name
        bio = this@asModel.bio
        location = this@asModel.location
        blog = this@asModel.blog
    }
}