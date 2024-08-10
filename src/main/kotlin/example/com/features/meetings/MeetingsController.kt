package example.com.features.meetings

import database.meetings.Meetings
import database.tokens.Tokens
import database.users.Users
import example.com.features.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class MeetingsController(private val call: ApplicationCall) {

    suspend fun fetchAllMeetings() {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meetings = Meetings.getAllMeetings(userDTO.id)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchMyMeetings() {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meetings = Meetings.getUserMeetings(userDTO.id)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchActiveMeetings() {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meetings = Meetings.getActiveMeetings(userDTO.id)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchFinishedMeetings() {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meetings = Meetings.getFinishedMeetings(userDTO.id)
        call.respond(HttpStatusCode.OK, MeetingsResponseRemote(true, meetings))
    }

    suspend fun fetchMeetingById(meetingID: String) {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meeting = Meetings.getMeetingById(meetingID, userDTO.id)
        when(meeting == null) {
            true -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Эвент не найден"))
            false -> call.respond(HttpStatusCode.OK, MeetingByIdResponseRemote(true, meeting))
        }
    }

    suspend fun toggleAttendance(meetingID: String) {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Отсутствует токен"))
            return
        }

        val userId = Tokens.fetchUserIdByToken(token)
        if (userId == null) {
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверный токен"))
            return
        }

        val userDTO = Users.fetchUser(userId)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден"))
            return
        }

        val meeting = Meetings.getMeetingById(meetingID, userDTO.id)
        if (meeting == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Встреча не найдена"))
            return
        }

        val updatedMeeting = Meetings.toggleAttendance(meetingID, userDTO.id)
        call.respond(HttpStatusCode.OK, MeetingByIdResponseRemote(true, updatedMeeting))
    }
}