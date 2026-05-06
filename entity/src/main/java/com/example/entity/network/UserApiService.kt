package com.example.entity.network

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.user.UpdateProfileRequest
import com.example.entity.model.user.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {

    @GET("api/v1/users/me")
    suspend fun getProfile(): Response<BaseResponse<UserProfile>>

    @PATCH("/api/v1/users/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<BaseResponse<UserProfile>>
}
