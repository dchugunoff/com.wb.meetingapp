package database.communities

import database.meetings.MeetingDTO
import kotlinx.serialization.Serializable

@Serializable
data class CommunityDTO(
    val id: String,
    val name: String,
    val size: String,
    val avatar: String?,
    val description: String,
    val meetings: List<MeetingDTO>
)
