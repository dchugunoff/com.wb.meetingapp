package example.com.features.communities

import database.communities.Communities
import database.tokens.Tokens
import database.users.Users
import example.com.features.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class CommunitiesController(private val call: ApplicationCall) {

    suspend fun fetchCommunityById(communityID: String) {
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

        val community = Communities.getCommunityById(communityId = communityID)
        when (community == null) {
            true -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Встреча не найдена"))
            false -> call.respond(HttpStatusCode.OK, CommunityByIdResponseRemote(true, community))
        }
    }

    suspend fun fetchAllCommunities() {
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

        val communities = Communities.getAllCommunities()
        call.respond(HttpStatusCode.OK, CommunitiesResponseRemote(true, communities))
    }
}