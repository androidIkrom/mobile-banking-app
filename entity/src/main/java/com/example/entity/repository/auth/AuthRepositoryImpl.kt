package com.example.entity.repository.auth

import com.example.entity.local.PrefsManager
import com.example.entity.model.auth.AuthData
import com.example.entity.model.auth.RefreshTokenRequest
import com.example.entity.model.auth.SendOtpRequest
import com.example.entity.model.auth.SetPinRequest
import com.example.entity.model.auth.VerifyOtpRequest
import com.example.entity.network.auth.AuthApiService
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val prefs: PrefsManager
) : AuthRepository {

    override suspend fun sendOtp(phone: String): Result<Unit> {
        return try {
            val response = api.sendOtp(SendOtpRequest(phone))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("OTP send failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(phone: String, otp: String): Result<AuthData> {
        return try {
            val response = api.verifyOtp(VerifyOtpRequest(phone, otp))
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                val authData = body.data
                prefs.accessToken = authData.accessToken
                prefs.refreshToken = authData.refreshToken
                prefs.isNewUser = authData.isNewUser
                Result.success(authData)
            } else {
                Result.failure(Exception(body?.error?.message ?: "Verification failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthData> {
        return try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                val authData = body.data
                prefs.accessToken = authData.accessToken
                prefs.refreshToken = authData.refreshToken
                prefs.isNewUser = authData.isNewUser
                Result.success(authData)
            } else {
                Result.failure(Exception(body?.error?.message ?: "Refresh failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setPin(pin: String): Result<Unit> {
        return try {
            val response = api.setPin(SetPinRequest(pin))
            val body = response.body()
            if (response.isSuccessful && body?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(body?.error?.message ?: "PIN set failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
