package example.com.features

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: String)