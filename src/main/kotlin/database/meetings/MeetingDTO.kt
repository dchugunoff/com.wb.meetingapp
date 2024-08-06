package database.meetings

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDTO(
    val id: String,
    val name: String,
    val date: String,
    val location: String,
    val tagList: List<String>,
    val meetingUrl: String?,
    val isFinished: Boolean,
    val isAttending: Boolean,
    val description: String?,
    val participants: List<UserResponseModelDTO>
)