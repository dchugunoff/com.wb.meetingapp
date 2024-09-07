package example.com.features.updateUserInfo

import database.users.UserResponseModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserReceiveRemote(
    val name: String? = null,
    val avatar: String? = null,
    val city: String? = null,
    val aboutUser: String? = null,
    val tagsInterests: List<String>? = null,
    val telegramNickname: String? = null,
    val habrNickname: String? = null
)

@Serializable
data class UpdateUserResponseRemote(
    val success: Boolean,
    val data: UserResponseModelDTO
)