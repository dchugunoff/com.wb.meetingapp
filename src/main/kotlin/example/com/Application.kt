package example.com

import example.com.features.communities.configureCommunitiesRouting
import example.com.features.login.configureLoginRouting
import example.com.features.meetings.configureMeetingsRouting
import example.com.features.updateUserInfo.configureUpdateUserRouting
import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        url = "jdbc:postgresql://dpg-cqp5kl2j1k6c73ddk5a0-a.oregon-postgres.render.com:5432/wb_meetings_app_database",
        driver = "org.postgresql.Driver",
        password = "c90zAYlnaaDELBJXrPbBUlN8LRhoD7HI",
        user = "wb_meetings_app_database_user"
    )

    embeddedServer(Netty, port = 8080, module = Application::module)
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
