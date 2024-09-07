package database.meetings

import database.communities.Communities
import database.communities.CommunityDTO
import database.communities.CommunitySubscriptions
import database.users.UserResponseModelDTO
import database.users.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Meetings : Table("meetings") {
    val id = varchar("id", 100)
    private val name = varchar("name", 100)
    private val date = varchar("date", 50)
    private val location = varchar("location", 100)
    private val tagList = text("tag_list")
    private val meetingAvatarUrl = varchar("meetingAvatarUrl", 200)
    private val isFinished = bool("isFinished")
    private val description = varchar("description", 300)
    private val communityId = varchar("community_id", 100).references(Communities.id)
    private val presenterID = varchar("presenter_user_id", 100).references(Users.id)
    private val presenterDescription = varchar("presenter_description", 200)
    private val places = integer("places")

    fun getAllMeetings(userId: String?): List<MeetingDTO> {
        return transaction {
            val meetings = Meetings.selectAll().map { meetingRow ->
                val meetingId = meetingRow[Meetings.id]

                val tags = meetingRow[tagList]
                val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                val presenter = Users.select { Users.id eq meetingRow[Meetings.presenterID] }
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
                    }.single()

                val isAttending = if (userId != null) {
                    MeetingParticipants.select {
                        (MeetingParticipants.meetingId eq meetingId) and
                                (MeetingParticipants.userId eq userId)
                    }.count() > 0
                } else {
                    false
                }

                val communityPresenter = Communities.select { Communities.id eq meetingRow[Meetings.communityId] }
                    .map {
                        val communityTags = it[Communities.tagList]
                        val communityTagsList =
                            communityTags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        val isSubscribed = if (userId != null) {
                            CommunitySubscriptions.select {
                                (CommunitySubscriptions.communityID eq it[Communities.id]) and
                                        (CommunitySubscriptions.userID eq userId)
                            }.count() > 0
                        } else {
                            false
                        }

                        CommunityDTO(
                            id = it[Communities.id],
                            name = it[Communities.name],
                            description = it[Communities.description],
                            avatar = it[Communities.avatar],
                            isSubscribed = isSubscribed,
                            tagList = communityTagsList
                        )
                    }.single()

                val participantsCount = MeetingParticipants.select { MeetingParticipants.meetingId eq meetingId }.count().toInt()
                val remainingPlaces: Int = meetingRow[places] - participantsCount

                MeetingDTO(
                    id = meetingId,
                    name = meetingRow[name],
                    date = meetingRow[date],
                    location = meetingRow[location],
                    tagList = tagList,
                    avatarUrl = meetingRow[meetingAvatarUrl],
                    isFinished = meetingRow[isFinished],
                    isAttending = isAttending,
                    presenter = presenter,
                    presenterDescription = meetingRow[presenterDescription],
                    communityPresenter = communityPresenter,
                    description = meetingRow[description],
                    remainingPlaces = remainingPlaces
                )
            }

            meetings
        }
    }

    fun getMeetingById(meetingId: String, userId: String?): MeetingDTO? {
        return transaction {
            val meetingRow = Meetings.select { Meetings.id eq meetingId }.singleOrNull()

            meetingRow?.let { row ->
                val tags = meetingRow[tagList]
                val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                val presenter = Users.select { Users.id eq meetingRow[Meetings.presenterID] }
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
                    }.single()

                val isAttending = if (userId != null) {
                    MeetingParticipants.select {
                        (MeetingParticipants.meetingId eq meetingId) and
                                (MeetingParticipants.userId eq userId)
                    }.count() > 0
                } else {
                    false
                }

                val communityPresenter = Communities.select { Communities.id eq meetingRow[Meetings.communityId] }
                    .map {
                        val communityTags = it[Communities.tagList]
                        val communityTagsList =
                            communityTags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        val isSubscribed = if (userId != null) {
                            CommunitySubscriptions.select {
                                (CommunitySubscriptions.communityID eq it[Communities.id]) and
                                        (CommunitySubscriptions.userID eq userId)
                            }.count() > 0
                        } else {
                            false
                        }

                        CommunityDTO(
                            id = it[Communities.id],
                            name = it[Communities.name],
                            description = it[Communities.description],
                            avatar = it[Communities.avatar],
                            isSubscribed = isSubscribed,
                            tagList = communityTagsList
                        )
                    }.single()

                val participantsCount = MeetingParticipants.select { MeetingParticipants.meetingId eq meetingId }.count().toInt()
                val remainingPlaces: Int = meetingRow[places] - participantsCount

                MeetingDTO(
                    id = row[Meetings.id],
                    name = row[name],
                    date = row[date],
                    location = row[location],
                    tagList = tagList,
                    avatarUrl = row[meetingAvatarUrl],
                    isFinished = row[isFinished],
                    isAttending = isAttending,
                    description = row[description],
                    presenter = presenter,
                    presenterDescription = row[presenterDescription],
                    communityPresenter = communityPresenter,
                    remainingPlaces = remainingPlaces
                )
            }
        }
    }

    fun getActiveMeetingsByCommunityId(communityId: String, userId: String?): List<MeetingDTO> {
        return transaction {
            val meetings = Meetings
                .select { (Meetings.communityId eq communityId) and (Meetings.isFinished eq false) }
                .map { meetingRow ->
                    val meetingId = meetingRow[Meetings.id]

                    val tags = meetingRow[tagList]
                    val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                    val presenter = Users.select { Users.id eq meetingRow[Meetings.presenterID] }
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
                        }.single()

                    val isAttending = if (userId != null) {
                        MeetingParticipants.select {
                            (MeetingParticipants.meetingId eq meetingId) and
                                    (MeetingParticipants.userId eq userId)
                        }.count() > 0
                    } else {
                        false
                    }

                    val communityPresenter = Communities.select { Communities.id eq meetingRow[Meetings.communityId] }
                        .map {
                            val communityTags = it[Communities.tagList]
                            val communityTagsList =
                                communityTags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                            val isSubscribed = if (userId != null) {
                                CommunitySubscriptions.select {
                                    (CommunitySubscriptions.communityID eq it[Communities.id]) and
                                            (CommunitySubscriptions.userID eq userId)
                                }.count() > 0
                            } else {
                                false
                            }

                            CommunityDTO(
                                id = it[Communities.id],
                                name = it[Communities.name],
                                description = it[Communities.description],
                                avatar = it[Communities.avatar],
                                isSubscribed = isSubscribed,
                                tagList = communityTagsList
                            )
                        }.single()

                    val participantsCount = MeetingParticipants.select { MeetingParticipants.meetingId eq meetingId }.count().toInt()
                    val remainingPlaces: Int = meetingRow[places] - participantsCount

                    MeetingDTO(
                        id = meetingId,
                        name = meetingRow[name],
                        date = meetingRow[date],
                        location = meetingRow[location],
                        tagList = tagList,
                        avatarUrl = meetingRow[meetingAvatarUrl],
                        isFinished = meetingRow[isFinished],
                        isAttending = isAttending,
                        presenter = presenter,
                        presenterDescription = meetingRow[presenterDescription],
                        communityPresenter = communityPresenter,
                        description = meetingRow[description],
                        remainingPlaces = remainingPlaces
                    )
                }

            meetings
        }
    }

    fun getFinishedMeetingsByCommunityId(communityId: String, userId: String?): List<MeetingDTO> {
        return transaction {
            val meetings = Meetings
                .select { (Meetings.communityId eq communityId) and (Meetings.isFinished eq true) }
                .map { meetingRow ->
                    val meetingId = meetingRow[Meetings.id]

                    val tags = meetingRow[tagList]
                    val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                    val presenter = Users.select { Users.id eq meetingRow[Meetings.presenterID] }
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
                        }.single()

                    val isAttending = if (userId != null) {
                        MeetingParticipants.select {
                            (MeetingParticipants.meetingId eq meetingId) and
                                    (MeetingParticipants.userId eq userId)
                        }.count() > 0
                    } else {
                        false
                    }

                    val communityPresenter = Communities.select { Communities.id eq meetingRow[Meetings.communityId] }
                        .map {
                            val communityTags = it[Communities.tagList]
                            val communityTagsList =
                                communityTags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                            val isSubscribed = if (userId != null) {
                                CommunitySubscriptions.select {
                                    (CommunitySubscriptions.communityID eq it[Communities.id]) and
                                            (CommunitySubscriptions.userID eq userId)
                                }.count() > 0
                            } else {
                                false
                            }

                            CommunityDTO(
                                id = it[Communities.id],
                                name = it[Communities.name],
                                description = it[Communities.description],
                                avatar = it[Communities.avatar],
                                isSubscribed = isSubscribed,
                                tagList = communityTagsList
                            )
                        }.single()

                    val participantsCount = MeetingParticipants.select { MeetingParticipants.meetingId eq meetingId }.count().toInt()
                    val remainingPlaces: Int = meetingRow[places] - participantsCount

                    MeetingDTO(
                        id = meetingId,
                        name = meetingRow[name],
                        date = meetingRow[date],
                        location = meetingRow[location],
                        tagList = tagList,
                        avatarUrl = meetingRow[meetingAvatarUrl],
                        isFinished = meetingRow[isFinished],
                        isAttending = isAttending,
                        presenter = presenter,
                        presenterDescription = meetingRow[presenterDescription],
                        communityPresenter = communityPresenter,
                        description = meetingRow[description],
                        remainingPlaces = remainingPlaces
                    )
                }

            meetings
        }
    }

    fun getParticipantsByMeetingId(meetingId: String): List<UserResponseModelDTO> {
        return transaction {
            val participants = MeetingParticipants
                .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                .select { MeetingParticipants.meetingId eq meetingId }
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

    fun toggleMeetingAttendance(meetingID: String, userId: String): List<UserResponseModelDTO> {
        return transaction {
            val participantExists = MeetingParticipants
                .select {
                    (MeetingParticipants.meetingId eq meetingID) and
                            (MeetingParticipants.userId eq userId)
                }.count() > 0

            if (participantExists) {
                MeetingParticipants.deleteWhere {
                    (meetingId eq meetingID) and (MeetingParticipants.userId eq userId)
                }
            } else {
                MeetingParticipants.insert {
                    it[meetingId] = meetingID
                    it[this.userId] = userId
                }
            }

            val participants = MeetingParticipants
                .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                .select { MeetingParticipants.meetingId eq meetingID }
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

    fun getMeetingsByUserId(userId: String): List<MeetingDTO> {
        return transaction {
            val meetingIds = MeetingParticipants
                .select { MeetingParticipants.userId eq userId }
                .map { it[MeetingParticipants.meetingId] }

            if (meetingIds.isNotEmpty()) {
                Meetings
                    .select { Meetings.id inList meetingIds }
                    .map { meetingRow ->
                        val meetingId = meetingRow[Meetings.id]

                        val tags = meetingRow[tagList]
                        val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        val presenter = Users.select { Users.id eq meetingRow[presenterID] }
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
                            }.single()

                        val communityPresenter = Communities.select { Communities.id eq meetingRow[Meetings.communityId] }
                            .map {
                                val communityTags = it[Communities.tagList]
                                val communityTagsList =
                                    communityTags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                                val isSubscribed = CommunitySubscriptions.select {
                                    (CommunitySubscriptions.communityID eq it[Communities.id]) and
                                            (CommunitySubscriptions.userID eq userId)
                                }.count() > 0

                                CommunityDTO(
                                    id = it[Communities.id],
                                    name = it[Communities.name],
                                    description = it[Communities.description],
                                    avatar = it[Communities.avatar],
                                    isSubscribed = isSubscribed,
                                    tagList = communityTagsList
                                )
                            }.single()

                        val participantsCount = MeetingParticipants.select { MeetingParticipants.meetingId eq meetingId }.count().toInt()
                        val remainingPlaces: Int = meetingRow[places] - participantsCount

                        MeetingDTO(
                            id = meetingId,
                            name = meetingRow[name],
                            date = meetingRow[date],
                            location = meetingRow[location],
                            tagList = tagList,
                            avatarUrl = meetingRow[meetingAvatarUrl],
                            isFinished = meetingRow[isFinished],
                            isAttending = false,
                            presenter = presenter,
                            presenterDescription = meetingRow[presenterDescription],
                            communityPresenter = communityPresenter,
                            description = meetingRow[description],
                            remainingPlaces = remainingPlaces
                        )
                    }
            } else {
                emptyList()
            }
        }
    }
}