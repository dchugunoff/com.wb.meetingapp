package example.com.features.updateUserInfo

import database.tokens.Tokens
import database.users.Users
import database.users.toResponseModel
import example.com.features.ErrorResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File

fun Application.configureUpdateUserRouting() {

    routing {
        static("/uploads") {
            files("uploads")
        }
    }

    routing {
        post("/update") {
            val receive = call.receive<UpdateUserReceiveRemote>()
            val updateUserController = UpdateUserInformationController(call)
            updateUserController.updateUserInfo(receive)
        }
    }

    routing {
        get("/me") {
            UpdateUserInformationController(call).getMe()
        }
    }
    routing {
        post("/uploadAvatar") {
            val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

            if (token == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
                return@post
            }

            val userId = Tokens.fetchUserIdByToken(token)
            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
                return@post
            }

            val contentType = call.request.contentType()
            if (contentType.match(ContentType.MultiPart.FormData)) {
                val multipart = call.receiveMultipart()
                var fileName: String? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val fileBytes = part.streamProvider().readBytes()
                        val extension = File(part.originalFileName).extension
                        fileName = "avatar_$userId.$extension"

                        val folder = File("uploads/avatars")
                        if (!folder.exists()) {
                            folder.mkdirs()
                        }

                        val file = File(folder, fileName!!)
                        file.writeBytes(fileBytes)
                    }
                    part.dispose()
                }

                if (fileName != null) {
                    transaction {
                        Users.update({ Users.id eq userId }) {
                            it[avatar] = "/uploads/avatars/$fileName"
                        }
                    }

                    val updatedUser = Users.fetchUser(userId)
                    if (updatedUser != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            UpdateUserResponseRemote(success = true, data = updatedUser.toResponseModel())
                        )
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Не удалось обновить пользователя"))
                    }
                } else {
                    call.respondText("Failed to upload avatar", status = HttpStatusCode.BadRequest)
                }
            } else {
                call.respondText("Invalid Content-Type", status = HttpStatusCode.BadRequest)
            }
        }
    }
}