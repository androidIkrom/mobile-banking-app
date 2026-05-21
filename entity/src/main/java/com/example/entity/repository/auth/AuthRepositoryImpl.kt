package com.example.entity.repository.auth

import com.example.entity.local.PrefsManager
import com.example.entity.model.auth.AuthData
import com.example.entity.model.auth.RefreshTokenRequest
import com.example.entity.model.auth.SendOtpRequest
import com.example.entity.model.auth.SetPinRequest
import com.example.entity.model.auth.VerifyOtpRequest
import com.example.entity.network.auth.AuthApiService
import com.example.entity.network.toResult
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val prefs: PrefsManager
) : AuthRepository {

    override suspend fun sendOtp(phone: String): Result<Unit> = try {
        api.sendOtp(SendOtpRequest(phone)).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun verifyOtp(phone: String, otp: String): Result<AuthData> = try {
        val result = api.verifyOtp(VerifyOtpRequest(phone, otp)).toResult()
        result.onSuccess { authData ->
            prefs.accessToken = authData.accessToken
            prefs.refreshToken = authData.refreshToken
            prefs.isNewUser = authData.isNewUser
        }
        result
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthData> = try {
        val result = api.refreshToken(RefreshTokenRequest(refreshToken)).toResult()
        result.onSuccess { authData ->
            prefs.accessToken = authData.accessToken
            prefs.refreshToken = authData.refreshToken
            prefs.isNewUser = authData.isNewUser
        }
        result
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setPin(pin: String): Result<Unit> = try {
        api.setPin(SetPinRequest(pin)).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
