package example.com.features.login

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val phoneNumber: String,
    val code: String
)

@Serializable
data class LoginResponseRemote(
    val success: Boolean, val userModel: UserResponseModelDTO, val token: String
)