package example.com.features.updateUserInfo

import database.tokens.Tokens
import database.users.Users
import database.users.toResponseModel
import example.com.features.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UpdateUserInformationController(private val call: ApplicationCall) {

    suspend fun updateUserInfo(updateUserReceiveRemote: UpdateUserReceiveRemote) {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val updatedUser = userDTO.copy(
            name = updateUserReceiveRemote.name ?: userDTO.name,
            avatar = updateUserReceiveRemote.avatar ?: userDTO.avatar,
            city = updateUserReceiveRemote.city ?: userDTO.city,
            aboutUser = updateUserReceiveRemote.aboutUser ?: userDTO.aboutUser,
            tagsInterests = updateUserReceiveRemote.tagsInterests ?: userDTO.tagsInterests,
            telegramNickname = updateUserReceiveRemote.telegramNickname ?: userDTO.telegramNickname,
            habrNickname = updateUserReceiveRemote.habrNickname ?: userDTO.habrNickname
        )

        Users.update(userId, updatedUser)

        call.respond(
            HttpStatusCode.OK,
            UpdateUserResponseRemote(success = true, data = updatedUser.toResponseModel())
        )
    }

    suspend fun getMe() {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        call.respond(
            HttpStatusCode.OK,
            userDTO
        )
    }
}