package com.medicao0102.vidaflow2.data
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class StatusResponse(
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String
)


@Serializable
data class ErrorResponse(
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String
)



@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)


@Serializable
data class LoginResponse(
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("name")
    val name: String,
    @SerialName("token")
    val token: String,
    @SerialName("user_id")
    val userId: Int
)
@Serializable
data class RegisterRequest(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String
)


@Serializable
data class RegisterResponse(
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String,
    @SerialName("user_id")
    val userId: Int
)