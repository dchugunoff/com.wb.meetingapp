package example.com.features.updateUserInfo

import database.tokens.Tokens
import database.users.Users
import database.users.toResponseModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UpdateUserInformationController(private val call: ApplicationCall) {

    suspend fun updateUserInfo(updateUserReceiveRemote: UpdateUserReceiveRemote) {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Отсутствует токен")
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Неверный токен")
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
            return
        }

        val updatedUser = userDTO.copy(
            firstName = updateUserReceiveRemote.firstName,
            secondName = updateUserReceiveRemote.secondName,
            hasForm = true
        )

        Users.update(userId, updatedUser)

        call.respond(
            HttpStatusCode.OK,
            UpdateUserResponseRemote(success = true, userModel = updatedUser.toResponseModel())
        )
    }
}