package example.com.features.communities

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Application.configureCommunitiesRouting() {
    routing {
        get("/fetchCommunityById") {
            val meetingId =
                call.request.queryParameters["community_id"] ?: throw BadRequestException("Сообщество не найдено")
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

    routing {
        get("/fetchUserCommunitiesById") {
            val userID =
                call.request.queryParameters["user_id"] ?: throw BadRequestException("Встречи не найдены")
            val communitiesController = CommunitiesController(call)
            communitiesController.fetchUserCommunitiesByID(userID)
        }
    }

    routing {
        get("/fetchCommunitySubscribers") {
            val communityID =
                call.request.queryParameters["community_id"] ?: throw BadRequestException("Сообщество не найдено")
            val communityController = CommunitiesController(call)
            communityController.fetchCommunitySubscribers(communityID)
        }
    }

    routing {
        post("/toggleCommunitySubscribe") {
            val communityID =
                call.request.queryParameters["community_id"] ?: throw BadRequestException("Сообщество не найдено")
            val communityController = CommunitiesController(call)
            communityController.toggleCommunitySubscribe(communityID)
        }
    }
}