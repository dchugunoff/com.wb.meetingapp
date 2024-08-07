package example.com.features.communities

import database.communities.CommunityDTO
import kotlinx.serialization.Serializable

@Serializable
data class CommunitiesResponseRemote(
    val success: Boolean, val communities: List<CommunityDTO>
)

@Serializable
data class CommunityByIdResponseRemote(
    val success: Boolean, val data: CommunityDTO
)