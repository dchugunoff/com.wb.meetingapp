package database.users

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseModelDTO (
    val id: String,
    val hasForm: Boolean,
    val phoneNumber: String,
    val firstName: String?,
    val secondName: String?,
    val avatar: String?
)