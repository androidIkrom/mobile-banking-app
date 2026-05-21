package com.example.entity.repository.loan

import com.example.entity.model.loan.ApplyLoanRequest
import com.example.entity.model.loan.LoanData
import com.example.entity.model.loan.RepayLoanRequest
import com.example.entity.network.loan.LoanApiService
import com.example.entity.network.toApiResult
import javax.inject.Inject

class LoanRepositoryImpl @Inject constructor(
    private val api: LoanApiService
) : LoanRepository {
    override suspend fun getLoans(): Result<List<LoanData>> = try {
        api.getLoans().toApiResult().map { it.data ?: emptyList() }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun applyLoan(request: ApplyLoanRequest): Result<LoanData> = try {
        api.applyLoan(request).toApiResult().mapCatching { 
            it.data ?: throw Exception("Ma'lumot topilmadi")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun repayLoan(id: String, request: RepayLoanRequest): Result<LoanData> = try {
        api.repayLoan(id, request).toApiResult().mapCatching { 
            it.data ?: throw Exception("Ma'lumot topilmadi")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
