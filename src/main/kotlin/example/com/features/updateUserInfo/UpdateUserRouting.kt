package example.com.features.updateUserInfo

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureUpdateUserRouting() {
    routing {
        post("/update") {
            val receive = call.receive<UpdateUserReceiveRemote>()
            val updateUserController = UpdateUserInformationController(call)
            updateUserController.updateUserInfo(receive)
        }
    }
}