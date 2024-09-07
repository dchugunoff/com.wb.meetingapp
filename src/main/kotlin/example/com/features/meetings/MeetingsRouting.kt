package example.com.features.meetings

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Application.configureMeetingsRouting() {
    routing {
        get("/fetchAllMeetings") {
            val meetingsController = MeetingsController(call)
            meetingsController.fetchAllMeetings()
        }
    }

    routing {
        get("/fetchMeetingById") {
            val meetingId =
                call.request.queryParameters["meeting_id"] ?: throw BadRequestException("Встреча не найдена")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchMeetingById(meetingId)
        }
    }

    routing {
        get("/fetchActiveMeetingsByCommunityId") {
            val meetingId =
                call.request.queryParameters["community_id"] ?: throw BadRequestException("Встречи не найдены")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchActiveMeetingsByCommunityID(meetingId)
        }
    }

    routing {
        get("/fetchFinishedMeetingsByCommunityId") {
            val meetingId =
                call.request.queryParameters["community_id"] ?: throw BadRequestException("Встречи не найдены")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchFinishedMeetingsByCommunityID(meetingId)
        }
    }

    routing {
        get("/fetchUsersByMeetingId") {
            val meetingId =
                call.request.queryParameters["meeting_id"] ?: throw BadRequestException("Встреча не найдена")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchUsersByMeetingID(meetingId)
        }
    }

    routing {
        post("/toggleMeetingAttendance") {
            val meetingId =
                call.request.queryParameters["meeting_id"] ?: throw BadRequestException("Встреча не найдена")
            val meetingsController = MeetingsController(call)
            meetingsController.toggleMeetingAttendance(meetingId)
        }
    }

    routing {
        get("/fetchUserMeetingsByUserId") {
            val userId = call.request.queryParameters["user_id"] ?: throw BadRequestException("Встречи не найдены")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchUserMeetingsByUserDI(userId)
        }
    }
}