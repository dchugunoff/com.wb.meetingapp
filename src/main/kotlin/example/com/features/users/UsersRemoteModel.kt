package example.com.features.users

import database.users.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserByIDRemoteModel(
    val success: Boolean, val data: UserDTO
)