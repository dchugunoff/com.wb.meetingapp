package example.com.features.communities

import database.communities.Communities
import database.meetings.Meetings
import database.tokens.Tokens
import database.users.Users
import example.com.features.ErrorResponse
import example.com.features.users.UserListResponseRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class CommunitiesController(private val call: ApplicationCall) {

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

    suspend fun fetchCommunityById(communityID: String) {
        val userID = checkUserToken()
        val community = Communities.getCommunityById(userID, communityID)
        when (community == null) {
            true -> call.respond(HttpStatusCode.NotFound, ErrorResponse("Встреча не найдена"))
            false -> call.respond(HttpStatusCode.OK, CommunityByIdResponseRemote(true, community))
        }
    }

    suspend fun fetchAllCommunities() {
        val userID = checkUserToken()
        val communities = Communities.getAllCommunities(userID)
        call.respond(HttpStatusCode.OK, CommunitiesResponseRemote(true, communities))
    }

    suspend fun fetchUserCommunitiesByID(userID: String) {
        val communities = Communities.getCommunitiesByUserId(userID)
        call.respond(HttpStatusCode.OK, CommunitiesResponseRemote(true, communities))
    }

    suspend fun fetchCommunitySubscribers(communityID: String) {
        val users = Communities.getCommunitySubscribers(communityID)
        call.respond(HttpStatusCode.OK, UserListResponseRemote(true, users))
    }

    suspend fun toggleCommunitySubscribe(communityID: String) {
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

        val meeting = Communities.getCommunityById(userDTO.id, communityID)
        if (meeting == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Встреча не найдена"))
            return
        }
        val users = Communities.toggleCommunitySubscribe(communityID, userId)
        call.respond(HttpStatusCode.OK, UserListResponseRemote(true, users))
    }
}