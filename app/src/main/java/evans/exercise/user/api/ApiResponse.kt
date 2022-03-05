package evans.exercise.user.api

import com.squareup.moshi.Json

/**
 * Created by evans.lai on 2022/3/5.
 */

data class UserResponse(
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "site_admin") val siteAdmin: Boolean,
)

data class UserDetailResponse(
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "site_admin") val siteAdmin: Boolean,
    @Json(name = "name") val name: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "blog") val blog: String?,
)