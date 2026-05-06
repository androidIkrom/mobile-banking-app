package com.example.entity.model.user

data class UserProfile(
    val id: String,
    val phone: String,
    val fullName: String?,
    val isKycVerified: Boolean,
    val createdAt: String
)

data class UpdateProfileRequest(
    val fullName: String
)
