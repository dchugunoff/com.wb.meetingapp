package database.meetings

import database.users.Users
import org.jetbrains.exposed.sql.Table

object MeetingParticipants : Table("meeting_participants") {
    val meetingId = varchar("meeting_id", 100).references(Meetings.id)
    val userId = varchar("user_id", 100).references(Users.id)
    override val primaryKey = PrimaryKey(meetingId, userId)
}