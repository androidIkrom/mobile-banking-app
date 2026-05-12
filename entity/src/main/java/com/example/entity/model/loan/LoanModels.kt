package com.example.entity.model.loan

data class LoanData(
    val id: String,
    val totalAmount: Long,
    val remaining: Long,
    val monthlyPayment: Long,
    val termMonths: Int,
    val nextDueDate: String,
    val status: String
)

data class LoanResponse(
    val success: Boolean,
    val data: List<LoanData>
)
