package example.com.features.login

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val phoneNumber: String
)

//@Serializable
//data class LoginResponseRemote(
//    val success: Boolean, val userModel: UserResponseModelDTO, val token: String
//)

@Serializable
data class LoginResponseRemote(
    val success: Boolean, val data: UserLoginResponseData
)

@Serializable
data class UserLoginResponseData(
    val userModel: UserResponseModelDTO,
    val token: String
)