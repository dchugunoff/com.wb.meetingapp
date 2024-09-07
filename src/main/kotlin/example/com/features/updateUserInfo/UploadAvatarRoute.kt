package example.com.features.updateUserInfo

import database.users.Users
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File

fun Route.uploadAvatar() {
    post("/uploadAvatar") {
        val userId = call.request.queryParameters["user_id"] ?: throw BadRequestException("Не указан userID")
        val multipart = call.receiveMultipart()
        var fileName: String? = null

        if (userId != null) {
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileBytes = part.streamProvider().readBytes()  // Получаем байты файла
                    val extension = File(part.originalFileName).extension // Извлекаем расширение файла
                    fileName = "avatar_$userId.$extension"  // Создаем уникальное имя для файла

                    val folder = File("uploads/avatars")  // Указываем путь для хранения аватаров
                    if (!folder.exists()) {
                        folder.mkdirs()  // Создаем папку, если ее нет
                    }

                    val file = File(folder, fileName!!)
                    file.writeBytes(fileBytes)  // Сохраняем файл на диск
                }
                part.dispose()
            }

            if (fileName != null) {
                // Сохраняем ссылку на аватар в базу данных
                transaction {
                    Users.update({ Users.id eq userId }) {
                        it[avatar] = "/uploads/avatars/$fileName"  // Сохраняем путь к файлу в базу
                    }
                }
                call.respondText("Avatar uploaded successfully", status = HttpStatusCode.OK)
            } else {
                call.respondText("Failed to upload avatar", status = HttpStatusCode.BadRequest)
            }
        } else {
            call.respondText("User ID is missing", status = HttpStatusCode.BadRequest)
        }
    }
}