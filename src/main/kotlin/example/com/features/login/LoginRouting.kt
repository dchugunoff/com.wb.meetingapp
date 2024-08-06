package example.com.features.login

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            val loginController = LoginController(call)
            loginController.loginUser(receive)
        }
    }
}