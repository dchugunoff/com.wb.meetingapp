package database.users

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val phoneNumber: String,
    val name: String? = null,
    val avatar: String? = null,
    val city: String? = null,
    val aboutUser: String? = null,
    val tagsInterests: List<String>? = null,
    val telegramNickname: String? = null,
    val habrNickname: String? = null
)

fun UserDTO.toResponseModel() = UserResponseModelDTO(
    id, phoneNumber, name, avatar, city, aboutUser, tagsInterests, telegramNickname, habrNickname
)