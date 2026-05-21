package com.example.entity.repository.user

import com.example.entity.model.transaction.TransactionData
import com.example.entity.model.user.UpdateProfileRequest
import com.example.entity.model.user.UserProfile
import com.example.entity.network.user.UserApiService
import com.example.entity.network.toApiResult
import com.example.entity.network.toResult
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getProfile(): Result<UserProfile> = try {
        userApiService.getProfile().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProfile(fullName: String): Result<UserProfile> = try {
        userApiService.updateProfile(UpdateProfileRequest(fullName)).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getTransactionHistory(
        page: Int,
        pageSize: Int,
        cardId: String?,
        type: String?
    ): Result<List<TransactionData>> = try {
        userApiService.getTransactionHistory(page, pageSize, cardId, type).toApiResult().map { 
            it.data ?: emptyList()
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
