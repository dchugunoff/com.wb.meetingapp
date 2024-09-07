package example.com.features.users

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserListResponseRemote(
    val success: Boolean,
    val data: List<UserResponseModelDTO>
)
