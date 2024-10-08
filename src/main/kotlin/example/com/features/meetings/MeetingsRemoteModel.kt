package example.com.features.meetings

import database.meetings.MeetingDTO
import kotlinx.serialization.Serializable

@Serializable
data class MeetingsResponseRemote(
    val success: Boolean, val data: List<MeetingDTO>
)

@Serializable
data class MeetingByIdResponseRemote(
    val success: Boolean, val data: MeetingDTO
)