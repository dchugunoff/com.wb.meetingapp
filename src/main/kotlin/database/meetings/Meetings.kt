package database.meetings

import database.communities.Communities
import database.users.UserResponseModelDTO
import database.users.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Meetings : Table("meetings") {
    val id = varchar("id", 100)
    val name = varchar("name", 50)
    val date = varchar("date", 50)
    val location = varchar("location", 100)
    val tagList = text("tag_list")
    val meetingAvatarUrl = varchar("meetingAvatarUrl", 200)
    val isFinished = bool("isFinished")
    val description = varchar("description", 300)
    val communityId = varchar("community_id", 100).references(Communities.id)

    fun getAllMeetings(userId: String): List<MeetingDTO> {
        return transaction {
            val meetings = Meetings.selectAll().map { meetingRow ->
                val meetingId = meetingRow[Meetings.id]

                val participants = MeetingParticipants
                    .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                    .select { MeetingParticipants.meetingId eq meetingId }
                    .map {
                        UserResponseModelDTO(
                            id = it[Users.id],
                            avatar = it[Users.avatar],
                            hasForm = it[Users.hasForm],
                            firstName = it[Users.firstName],
                            secondName = it[Users.secondName],
                            phoneNumber = it[Users.phoneNumber]
                        )
                    }

                val isAttending = participants.any { it.id == userId }

                val tags = meetingRow[tagList]
                val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                MeetingDTO(
                    id = meetingId,
                    name = meetingRow[name],
                    date = meetingRow[date],
                    location = meetingRow[location],
                    tagList = tagList,
                    meetingUrl = meetingRow[meetingAvatarUrl],
                    isFinished = meetingRow[isFinished],
                    isAttending = isAttending,
                    description = meetingRow[description],
                    participants = participants
                )
            }

            meetings
        }
    }

    fun getUserMeetings(userId: String): List<MeetingDTO> {
        return transaction {

            val userMeetingIds = MeetingParticipants
                .select { MeetingParticipants.userId eq userId }
                .map { it[MeetingParticipants.meetingId] }
                .toSet()

            val meetings = Meetings
                .select { Meetings.id inList userMeetingIds }
                .map { meetingRow ->
                    val meetingId = meetingRow[Meetings.id]

                    val participants = MeetingParticipants
                        .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                        .select { MeetingParticipants.meetingId eq meetingId }
                        .map {
                            UserResponseModelDTO(
                                id = it[Users.id],
                                avatar = it[Users.avatar],
                                hasForm = it[Users.hasForm],
                                firstName = it[Users.firstName],
                                secondName = it[Users.secondName],
                                phoneNumber = it[Users.phoneNumber]
                            )
                        }

                    val isAttending = participants.any { it.id == userId }

                    val tags = meetingRow[tagList]
                    val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                    MeetingDTO(
                        id = meetingId,
                        name = meetingRow[name],
                        date = meetingRow[date],
                        location = meetingRow[location],
                        tagList = tagList,
                        meetingUrl = meetingRow[meetingAvatarUrl],
                        isFinished = meetingRow[isFinished],
                        isAttending = isAttending,
                        description = meetingRow[description],
                        participants = participants
                    )
                }

            meetings
        }
    }

    fun getActiveMeetings(userId: String): List<MeetingDTO> {
        return transaction {
            val meetings = Meetings
                .select { isFinished eq false }
                .map { meetingRow ->
                    val meetingId = meetingRow[Meetings.id]

                    val participants = MeetingParticipants
                        .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                        .select { MeetingParticipants.meetingId eq meetingId }
                        .map {
                            UserResponseModelDTO(
                                id = it[Users.id],
                                avatar = it[Users.avatar],
                                hasForm = it[Users.hasForm],
                                firstName = it[Users.firstName],
                                secondName = it[Users.secondName],
                                phoneNumber = it[Users.phoneNumber]
                            )
                        }


                    val isAttending = participants.any { it.id == userId }

                    val tags = meetingRow[tagList]
                    val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                    MeetingDTO(
                        id = meetingId,
                        name = meetingRow[name],
                        date = meetingRow[date],
                        location = meetingRow[location],
                        tagList = tagList,
                        meetingUrl = meetingRow[meetingAvatarUrl],
                        isFinished = meetingRow[isFinished],
                        isAttending = isAttending,
                        description = meetingRow[description],
                        participants = participants
                    )
                }

            meetings
        }
    }

    fun getFinishedMeetings(userId: String): List<MeetingDTO> {
        return transaction {

            val userMeetingIds = MeetingParticipants
                .select { MeetingParticipants.userId eq userId }
                .map { it[MeetingParticipants.meetingId] }
                .toSet()

            val meetings = Meetings
                .select { (Meetings.id inList userMeetingIds) and (Meetings.isFinished eq true) }
                .map { meetingRow ->
                    val meetingId = meetingRow[Meetings.id]

                    val participants = MeetingParticipants
                        .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                        .select { MeetingParticipants.meetingId eq meetingId }
                        .map {
                            UserResponseModelDTO(
                                id = it[Users.id],
                                avatar = it[Users.avatar],
                                hasForm = it[Users.hasForm],
                                firstName = it[Users.firstName],
                                secondName = it[Users.secondName],
                                phoneNumber = it[Users.phoneNumber]
                            )
                        }

                    val isAttending = participants.any { it.id == userId }

                    val tags = meetingRow[tagList]
                    val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                    MeetingDTO(
                        id = meetingId,
                        name = meetingRow[name],
                        date = meetingRow[date],
                        location = meetingRow[location],
                        tagList = tagList,
                        meetingUrl = meetingRow[meetingAvatarUrl],
                        isFinished = meetingRow[isFinished],
                        isAttending = isAttending,
                        description = meetingRow[description],
                        participants = participants
                    )
                }

            meetings
        }
    }

    fun getMeetingById(meetingId: String, userId: String): MeetingDTO? {
        return transaction {
            val meetingRow = Meetings.select { Meetings.id eq meetingId }.singleOrNull()

            meetingRow?.let { row ->
                val participants = MeetingParticipants
                    .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                    .select { MeetingParticipants.meetingId eq meetingId }
                    .map {
                        UserResponseModelDTO(
                            id = it[Users.id],
                            avatar = it[Users.avatar],
                            hasForm = it[Users.hasForm],
                            firstName = it[Users.firstName],
                            secondName = it[Users.secondName],
                            phoneNumber = it[Users.phoneNumber]
                        )
                    }

                val isAttending = participants.any { it.id == userId }

                val tags = row[tagList]
                val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                MeetingDTO(
                    id = row[Meetings.id],
                    name = row[name],
                    date = row[date],
                    location = row[location],
                    tagList = tagList,
                    meetingUrl = row[meetingAvatarUrl],
                    isFinished = row[isFinished],
                    isAttending = isAttending,
                    description = row[description],
                    participants = participants
                )
            }
        }
    }

    fun toggleAttendance(meetingId: String, userId: String): MeetingDTO {
        return transaction {
            val participantExists = MeetingParticipants.select {
                (MeetingParticipants.meetingId eq meetingId) and (MeetingParticipants.userId eq userId)
            }.count() > 0

            if (participantExists) {
                MeetingParticipants.deleteWhere {
                    (MeetingParticipants.meetingId eq meetingId) and (MeetingParticipants.userId eq userId)
                }
            } else {
                MeetingParticipants.insert {
                    it[MeetingParticipants.meetingId] = meetingId
                    it[MeetingParticipants.userId] = userId
                }
            }

            val meetingRow = Meetings.select { Meetings.id eq meetingId }.single()

            val participants = MeetingParticipants
                .join(Users, JoinType.INNER, MeetingParticipants.userId, Users.id)
                .select { MeetingParticipants.meetingId eq meetingId }
                .map {
                    UserResponseModelDTO(
                        id = it[Users.id],
                        avatar = it[Users.avatar],
                        hasForm = it[Users.hasForm],
                        firstName = it[Users.firstName],
                        secondName = it[Users.secondName],
                        phoneNumber = it[Users.phoneNumber]
                    )
                }

            val isAttending = participants.any { it.id == userId }

            val tags = meetingRow[tagList]
            val tagList = tags.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

            MeetingDTO(
                id = meetingRow[Meetings.id],
                name = meetingRow[name],
                date = meetingRow[date],
                location = meetingRow[location],
                tagList = tagList,
                meetingUrl = meetingRow[meetingAvatarUrl],
                isFinished = meetingRow[isFinished],
                isAttending = isAttending,
                description = meetingRow[description],
                participants = participants
            )
        }
    }
}