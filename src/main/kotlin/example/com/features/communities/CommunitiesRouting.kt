package example.com.features.communities

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Application.configureCommunitiesRouting() {
    routing {
        get("/fetchCommunityById") {
            val meetingId =
                call.request.queryParameters["communityId"] ?: throw BadRequestException("Сообщество не найдено")
            val communityController = CommunitiesController(call)
            communityController.fetchCommunityById(meetingId)
        }
    }

    routing {
        get("/fetchAllCommunities") {
            val communityController = CommunitiesController(call)
            communityController.fetchAllCommunities()
        }
    }
}