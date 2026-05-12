package com.example.entity.repository.loan

import com.example.entity.model.loan.LoanData
import com.example.entity.network.loan.LoanApiService
import javax.inject.Inject

class LoanRepositoryImpl @Inject constructor(
    private val api: LoanApiService
) : LoanRepository {
    override suspend fun getLoans(): Result<List<LoanData>> {
        return try {
            val response = api.getLoans()
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Kreditlarni yuklashda xatolik yuz berdi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
