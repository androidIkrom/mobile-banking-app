package com.example.usecase.impl.loan

import com.example.entity.model.loan.LoanData
import com.example.entity.model.loan.RepayLoanRequest
import com.example.entity.repository.loan.LoanRepository
import com.example.usecase.RepayLoanUseCase
import javax.inject.Inject

class RepayLoanUseCaseImpl @Inject constructor(
    private val repository: LoanRepository
) : RepayLoanUseCase {
    override suspend fun invoke(id: String, request: RepayLoanRequest): Result<LoanData> = repository.repayLoan(id, request)
}
