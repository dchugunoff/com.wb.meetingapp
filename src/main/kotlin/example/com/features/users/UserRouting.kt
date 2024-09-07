package example.com.features.users

import example.com.features.meetings.MeetingsController
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Application.configureUsersRouting() {
    routing {
        get("/fetchUserByID") {
            val userID =
                call.request.queryParameters["user_id"] ?: throw BadRequestException("Пользователь не найден")
            val usersController = UsersController(call)
            usersController.fetchUserByID(userID)
        }
    }
}