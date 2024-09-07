package example.com.features.meetings

import database.meetings.Meetings
import database.tokens.Tokens
import database.users.Users
import example.com.features.ErrorResponse
import example.com.features.users.UserListResponseRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

class MeetingsController(private val call: ApplicationCall) {

    private fun checkUserToken(): String? {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        when (token == null) {
            true -> {
                return null
            }

            false -> {
                val userId = Tokens.fetchUserIdByToken(token)
                when (userId == null) {
                    true -> {
                        return null
                    }

                    false -> {
                        val userDTO = Users.fetchUser(userId)
                        return when (userDTO == null) {
                            true -> {
                                null
                            }

                            false -> {
                                userDTO.id
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun fetchAllMeetings() {
        val userID = checkUserToken()
        val meetings = Meetings.getAllMeetings(userID)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchMeetingById(meetingID: String) {
        val userID = checkUserToken()

        val meeting = Meetings.getMeetingById(meetingID, userID)
        when (meeting == null) {
            true -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Событие не найдено"))
            false -> call.respond(HttpStatusCode.OK, MeetingByIdResponseRemote(true, meeting))
        }
    }

    suspend fun fetchActiveMeetingsByCommunityID(communityID: String) {
        val userID = checkUserToken()
        val meetings = Meetings.getActiveMeetingsByCommunityId(communityID, userID)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchFinishedMeetingsByCommunityID(communityID: String) {
        val userID = checkUserToken()
        val meetings = Meetings.getFinishedMeetingsByCommunityId(communityID, userID)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchUserMeetingsByUserDI(userID: String) {
        val meetings = Meetings.getMeetingsByUserId(userID)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchUsersByMeetingID(meetingID: String) {
        val users = Meetings.getParticipantsByMeetingId(meetingID)
        call.respond(HttpStatusCode.OK, UserListResponseRemote(true, users))
    }

    suspend fun toggleMeetingAttendance(meetingID: String) {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
            return
        }

        val meeting = Meetings.getMeetingById(meetingID, userDTO.id)
        if (meeting == null) {
            call.respond(HttpStatusCode.NotFound, "Встреча не найдена")
            return
        }
        val users = Meetings.toggleMeetingAttendance(meetingID, userId)
        call.respond(HttpStatusCode.OK, UserListResponseRemote(true, users))
    }
}