package database.meetings

import database.communities.CommunityDTO
import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDTO(
    val id: String,
    val name: String,
    val date: String,
    val location: String,
    val tagList: List<String>,
    val avatarUrl: String,
    val isFinished: Boolean,
    var isAttending: Boolean,
    val description: String,
    val presenter: UserResponseModelDTO,
    val presenterDescription: String,
    val communityPresenter: CommunityDTO,
    val remainingPlaces: Int
)