package database.communities

import database.meetings.MeetingDTO
import database.meetings.Meetings
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Communities : Table("communities") {
    val id = varchar("id", 100)
    private val name = varchar("name", 50)
    private val size = varchar("size", 8)
    private val avatar = varchar("avatar", 200)
    private val description = varchar("description", 300)

    fun getAllCommunities(): List<CommunityDTO> {
        return transaction {
            val communities = Communities.selectAll().map { communityRow ->
                val communityId = communityRow[Communities.id]

                val meetings = Meetings
                    .select { Meetings.communityId eq communityId }
                    .map { meetingRow ->
                        val tags = meetingRow[Meetings.tagList]
                        val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        MeetingDTO(
                            id = meetingRow[Meetings.id],
                            name = meetingRow[Meetings.name],
                            date = meetingRow[Meetings.date],
                            location = meetingRow[Meetings.location],
                            tagList = tagList,
                            meetingUrl = meetingRow[Meetings.meetingAvatarUrl],
                            isFinished = meetingRow[Meetings.isFinished],
                            isAttending = false,
                            description = meetingRow[Meetings.description],
                            participants = emptyList()
                        )
                    }

                CommunityDTO(
                    id = communityId,
                    name = communityRow[name],
                    size = communityRow[size],
                    avatar = communityRow[avatar],
                    description = communityRow[description],
                    meetings = meetings
                )
            }

            communities
        }
    }

    fun getCommunityById(communityId: String): CommunityDTO? {
        return transaction {
            val communityRow = Communities.select { Communities.id eq communityId }.singleOrNull()

            communityRow?.let { row ->
                val meetings = Meetings
                    .select { Meetings.communityId eq communityId }
                    .map { meetingRow ->
                        val tags = meetingRow[Meetings.tagList]
                        val tagList = tags?.takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim() } ?: emptyList()

                        MeetingDTO(
                            id = meetingRow[Meetings.id],
                            name = meetingRow[Meetings.name],
                            date = meetingRow[Meetings.date],
                            location = meetingRow[Meetings.location],
                            tagList = tagList,
                            meetingUrl = meetingRow[Meetings.meetingAvatarUrl],
                            isFinished = meetingRow[Meetings.isFinished],
                            isAttending = false,
                            description = meetingRow[Meetings.description],
                            participants = emptyList()
                        )
                    }

                CommunityDTO(
                    id = row[Communities.id],
                    name = row[name],
                    size = row[size],
                    avatar = row[avatar],
                    description = row[description],
                    meetings = meetings
                )
            }
        }
    }
}