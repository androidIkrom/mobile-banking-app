package com.example.entity.model.auth

data class SendOtpRequest(
    val phone: String
)

data class VerifyOtpRequest(
    val phone: String,
    val otp: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class BaseResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ApiError?
)

data class ApiError(
    val code: String,
    val message: String
)

data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean
)

data class SetPinRequest(
    val pin: String
)
