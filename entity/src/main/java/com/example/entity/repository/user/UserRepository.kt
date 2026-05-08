package com.example.entity.repository.user

import com.example.entity.model.transaction.TransactionData
import com.example.entity.model.user.UserProfile

interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(fullName: String): Result<UserProfile>
    suspend fun getTransactionHistory(
        page: Int,
        pageSize: Int,
        cardId: String?,
        type: String?
    ) : Result<List<TransactionData>>
}
