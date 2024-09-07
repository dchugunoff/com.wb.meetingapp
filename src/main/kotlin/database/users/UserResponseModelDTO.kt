package database.users

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseModelDTO (
    val id: String,
    val phoneNumber: String,
    val name: String?,
    val avatar: String?,
    val city: String?,
    val aboutUser: String?,
    val tagsInterests: List<String>?,
    val telegramNickname: String?,
    val habrNickname: String?
)