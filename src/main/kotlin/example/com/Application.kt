package example.com

import example.com.features.communities.configureCommunitiesRouting
import example.com.features.login.configureLoginRouting
import example.com.features.meetings.configureMeetingsRouting
import example.com.features.updateUserInfo.configureUpdateUserRouting
import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        url = System.getenv("DATABASE_URL"),
        driver = "org.postgresql.Driver",
        password = System.getenv("DATABASE_PASSWORD"),
        user = System.getenv("DATABASE_USER")
    )

    embeddedServer(CIO, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()
    configureUpdateUserRouting()
    configureMeetingsRouting()
    configureCommunitiesRouting()
}
