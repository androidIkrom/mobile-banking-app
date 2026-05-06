package com.example.entity.network

import com.example.entity.model.auth.AuthData
import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.auth.RefreshTokenRequest
import com.example.entity.model.auth.SendOtpRequest
import com.example.entity.model.auth.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/v1/auth/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<Unit>

    @POST("/api/v1/auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<BaseResponse<AuthData>>

    @POST("/api/v1/auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<BaseResponse<AuthData>>
}
