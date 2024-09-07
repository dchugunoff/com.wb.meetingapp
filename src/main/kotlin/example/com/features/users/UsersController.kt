package example.com.features.users

import database.users.Users
import example.com.features.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UsersController(private val call: ApplicationCall) {
    suspend fun fetchUserByID(userID: String) {

        val user = Users.fetchUser(userID)
        when (user == null) {
            true -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            false -> call.respond(HttpStatusCode.OK, UserByIDRemoteModel(true, user))
        }
    }
}