package database.communities

import database.users.UserResponseModelDTO
import database.users.Users
import database.users.Users.tags
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Communities : Table("communities") {
    val id = varchar("id", 100)
    val name = varchar("name", 100)
    val avatar = varchar("avatar", 200)
    val description = varchar("description", 300)
    val tagList = text("tag_list")

    fun getAllCommunities(userID: String?): List<CommunityDTO> {
        return transaction {
            val communities = Communities.selectAll().map { communityRow ->
                val communityId = communityRow[Communities.id]

                val tags = communityRow[tagList]
                val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                val isSubscribed = if (userID != null) {
                    CommunitySubscriptions.select {
                        (CommunitySubscriptions.communityID eq communityRow[Communities.id]) and
                                (CommunitySubscriptions.userID eq userID)
                    }.count() > 0
                } else {
                    false
                }

                CommunityDTO(
                    id = communityId,
                    name = communityRow[name],
                    avatar = communityRow[avatar],
                    description = communityRow[description],
                    tagList = tagList,
                    isSubscribed = isSubscribed
                )
            }

            communities
        }
    }

    fun getCommunityById(userID: String?, communityId: String): CommunityDTO? {
        return transaction {
            val communityRow = Communities.select { Communities.id eq communityId }.singleOrNull()

            communityRow?.let { row ->
                val tags = communityRow[tagList]
                val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                val isSubscribed = if (userID != null) {
                    CommunitySubscriptions.select {
                        (CommunitySubscriptions.communityID eq row[Communities.id]) and
                                (CommunitySubscriptions.userID eq userID)
                    }.count() > 0
                } else {
                    false
                }

                CommunityDTO(
                    id = row[Communities.id],
                    name = row[name],
                    avatar = row[avatar],
                    description = row[description],
                    tagList = tagList,
                    isSubscribed = isSubscribed
                )
            }
        }
    }

    fun getCommunitySubscribers(communityId: String): List<UserResponseModelDTO> {
        return transaction {
            val subscribers = CommunitySubscriptions
                .join(Users, JoinType.INNER, CommunitySubscriptions.userID, Users.id)
                .select { CommunitySubscriptions.communityID eq communityId }
                .map {
                    UserResponseModelDTO(
                        id = it[Users.id],
                        avatar = it[Users.avatar],
                        phoneNumber = it[Users.phoneNumber],
                        name = it[Users.name],
                        city = it[Users.city],
                        aboutUser = it[Users.aboutUser],
                        tagsInterests = it[Users.tags]?.split(","),
                        telegramNickname = it[Users.telegramNickname],
                        habrNickname = it[Users.habrNickname]
                    )
                }

            subscribers
        }
    }

    fun toggleCommunitySubscribe(communityId: String, userID: String): List<UserResponseModelDTO> {
        return transaction {
            val subscriberExist = CommunitySubscriptions
                .select {
                    (CommunitySubscriptions.communityID eq communityId) and
                            (CommunitySubscriptions.userID eq userID)
                }.count() > 0

            if (subscriberExist) {
                CommunitySubscriptions.deleteWhere {
                    (communityID eq communityId) and (CommunitySubscriptions.userID eq userID)
                }
            } else {
                CommunitySubscriptions.insert {
                    it[communityID] = communityId
                    it[this.userID] = userID
                }
            }

            val participants = CommunitySubscriptions
                .join(Users, JoinType.INNER, CommunitySubscriptions.userID, Users.id)
                .select { CommunitySubscriptions.communityID eq communityId }
                .map {
                    UserResponseModelDTO(
                        id = it[Users.id],
                        avatar = it[Users.avatar],
                        phoneNumber = it[Users.phoneNumber],
                        name = it[Users.name],
                        city = it[Users.city],
                        aboutUser = it[Users.aboutUser],
                        tagsInterests = it[Users.tags]?.split(","),
                        telegramNickname = it[Users.telegramNickname],
                        habrNickname = it[Users.habrNickname]
                    )
                }

            participants
        }
    }

    fun getCommunitiesByUserId(userID: String): List<CommunityDTO> {
        return transaction {
            val communityIds = CommunitySubscriptions
                .select { CommunitySubscriptions.userID eq userID }
                .map { it[CommunitySubscriptions.communityID] }

            if (communityIds.isNotEmpty()) {
                Communities
                    .select { Communities.id inList communityIds }
                    .map { communityRow ->
                        val communityId = communityRow[Communities.id]

                        val tags = communityRow[tagList]
                        val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        CommunityDTO(
                            id = communityId,
                            name = communityRow[name],
                            avatar = communityRow[avatar],
                            description = communityRow[description],
                            tagList = tagList,
                            isSubscribed = false
                        )
                    }
            } else {
                emptyList()
            }
        }
    }
}