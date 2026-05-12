package com.example.usecase.impl.loan

import com.example.entity.model.loan.LoanData
import com.example.entity.repository.loan.LoanRepository
import com.example.usecase.GetLoansUseCase
import javax.inject.Inject

class GetLoansUseCaseImpl @Inject constructor(
    private val repository: LoanRepository
) : GetLoansUseCase {
    override suspend fun invoke(): Result<List<LoanData>> = repository.getLoans()
}
