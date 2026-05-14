package com.example.entity.repository.loan

import com.example.entity.model.loan.ApplyLoanRequest
import com.example.entity.model.loan.LoanData
import com.example.entity.model.loan.RepayLoanRequest

interface LoanRepository {
    suspend fun getLoans(): Result<List<LoanData>>
    suspend fun applyLoan(request: ApplyLoanRequest): Result<LoanData>
    suspend fun repayLoan(id: String, request: RepayLoanRequest): Result<LoanData>
}
