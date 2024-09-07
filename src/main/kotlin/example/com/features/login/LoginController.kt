package example.com.features.login

import database.tokens.Tokens
import database.users.UserDTO
import database.users.Users
import database.users.toResponseModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun loginUser(loginReceiveRemote: LoginReceiveRemote) {
        val userDTO = Users.fetchUserByPhoneNumber(loginReceiveRemote.phoneNumber)

        if (userDTO == null) {
            val newUser = UserDTO(
                id = UUID.randomUUID().toString(),
                phoneNumber = loginReceiveRemote.phoneNumber,
            )
            Users.insert(newUser)
            val token = UUID.randomUUID().toString()
            Tokens.upsert(newUser.id, token)
            call.respond(
                HttpStatusCode.OK,
                LoginResponseRemote(
                    success = true,
                    data = UserLoginResponseData(userModel = newUser.toResponseModel(), token = token)
                )
            )
        } else {
            val token = UUID.randomUUID().toString()
            Tokens.upsert(userDTO.id, token)
            call.respond(
                HttpStatusCode.OK,
                LoginResponseRemote(
                    success = true,
                    data = UserLoginResponseData(userModel = userDTO.toResponseModel(), token = token)
                )
            )
        }
    }
}