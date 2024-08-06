package example.com.features.updateUserInfo

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserReceiveRemote(
    val firstName: String,
    val secondName: String
)

@Serializable
data class UpdateUserResponseRemote(
    val success: Boolean,
    val userModel: UserResponseModelDTO
)