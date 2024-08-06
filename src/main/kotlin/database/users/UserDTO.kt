package database.users

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val hasForm: Boolean,
    val phoneNumber: String,
    val pinCode: String,
    val firstName: String?,
    val secondName: String?,
    val avatar: String?
)

fun UserDTO.toResponseModel() = UserResponseModelDTO(
    id, hasForm, phoneNumber, firstName, secondName, avatar
)