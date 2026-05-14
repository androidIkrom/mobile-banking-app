package com.example.entity.model.loan

import com.example.entity.model.auth.ApiError
import com.example.entity.model.auth.ApiResponse

data class LoanData(
    val id: String,
    val totalAmount: Double,
    val remaining: Double,
    val monthlyPayment: Double,
    val termMonths: Int,
    val nextDueDate: String,
    val status: String
)

data class LoanResponse(
    override val success: Boolean,
    val data: List<LoanData>?,
    override val error: ApiError?
) : ApiResponse

data class LoanSingleResponse(
    override val success: Boolean,
    val data: LoanData?,
    override val error: ApiError?
) : ApiResponse

data class ApplyLoanRequest(
    val amount: Double,
    val termMonths: Int,
    val cardId: String
)

data class RepayLoanRequest(
    val cardId: String,
    val amount: Double
)
