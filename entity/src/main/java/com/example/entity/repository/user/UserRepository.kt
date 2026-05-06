package com.example.entity.repository.user

import com.example.entity.model.user.UserProfile

interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(fullName: String): Result<UserProfile>
}
