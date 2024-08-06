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
        get("/fetchMyMeetings") {
            val meetingsController = MeetingsController(call)
            meetingsController.fetchMyMeetings()
        }
    }

    routing {
        get("/fetchActiveMeetings") {
            val meetingsController = MeetingsController(call)
            meetingsController.fetchActiveMeetings()
        }
    }
    routing {
        get("/fetchFinishedMeetings") {
            val meetingsController = MeetingsController(call)
            meetingsController.fetchFinishedMeetings()
        }
    }
    routing {
        get("/fetchMeetingById") {
            val meetingId =
                call.request.queryParameters["meetingId"] ?: throw BadRequestException("Встреча не найдена")
            val meetingsController = MeetingsController(call)
            meetingsController.fetchMeetingById(meetingId)
        }
    }

    routing {
        post("/toggleAttendance") {
            val meetingId =
                call.request.queryParameters["meetingId"] ?: throw BadRequestException("Встреча не найдена")
            val meetingsController = MeetingsController(call)
            meetingsController.toggleAttendance(meetingId)
        }
    }
}