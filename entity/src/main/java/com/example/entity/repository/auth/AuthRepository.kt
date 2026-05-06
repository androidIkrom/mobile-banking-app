package com.example.entity.repository.auth

import com.example.entity.model.auth.AuthData

interface AuthRepository {
    suspend fun sendOtp(phone: String): Result<Unit>
    suspend fun verifyOtp(phone: String, otp: String): Result<AuthData>
    suspend fun refreshToken(refreshToken: String): Result<AuthData>
}
