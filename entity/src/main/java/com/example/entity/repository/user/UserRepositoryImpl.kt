package com.example.entity.repository.user

import com.example.entity.model.transaction.TransactionData
import com.example.entity.model.user.UpdateProfileRequest
import com.example.entity.model.user.UserProfile
import com.example.entity.network.user.UserApiService
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

    override suspend fun getTransactionHistory(
        page: Int,
        pageSize: Int,
        cardId: String?,
        type: String?
    ): Result<List<TransactionData>> {
        return try {
            val response = userApiService.getTransactionHistory(page,pageSize,cardId,type)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null){
                Result.success(body.data)
            }
            else{
                Result.failure(Exception("Tranzaksiyalarni yuklab bo`lmadi"))
            }
        }
        catch (e : Exception){
            Result.failure(e)
        }
    }
}
