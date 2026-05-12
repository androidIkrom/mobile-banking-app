package com.example.entity.repository.loan

import com.example.entity.model.loan.LoanData

interface LoanRepository {
    suspend fun getLoans(): Result<List<LoanData>>
}
