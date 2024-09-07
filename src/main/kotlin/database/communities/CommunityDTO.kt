package database.communities

import kotlinx.serialization.Serializable

@Serializable
data class CommunityDTO(
    val id: String,
    val name: String,
    val avatar: String,
    val description: String,
    val isSubscribed: Boolean,
    val tagList: List<String>,
)
