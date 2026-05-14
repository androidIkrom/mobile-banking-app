package com.example.usecase.impl.loan

import com.example.entity.model.loan.ApplyLoanRequest
import com.example.entity.model.loan.LoanData
import com.example.entity.repository.loan.LoanRepository
import com.example.usecase.ApplyLoanUseCase
import javax.inject.Inject

class ApplyLoanUseCaseImpl @Inject constructor(
    private val repository: LoanRepository
) : ApplyLoanUseCase {
    override suspend fun invoke(request: ApplyLoanRequest): Result<LoanData> = repository.applyLoan(request)
}
