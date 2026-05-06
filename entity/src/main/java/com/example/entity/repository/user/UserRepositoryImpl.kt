package com.example.entity.repository.user

import com.example.entity.model.user.UpdateProfileRequest
import com.example.entity.model.user.UserProfile
import com.example.entity.network.UserApiService
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = userApiService.getProfile()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.error?.message ?: "Unknown error"))
                }
            } else {
                Result.failure(Exception("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(fullName: String): Result<UserProfile> {
        return try {
            val response = userApiService.updateProfile(UpdateProfileRequest(fullName))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.error?.message ?: "Unknown error"))
                }
            } else {
                Result.failure(Exception("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
