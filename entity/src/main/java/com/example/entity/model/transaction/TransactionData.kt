package com.example.entity.model.transaction

import com.example.entity.model.auth.ApiError
import com.example.entity.model.auth.ApiResponse

data class TransactionData(
    val id: String,
    val type: String, // TRANSFER_IN, TRANSFER_OUT, PAYMENT, etc.
    val amount: Double,
    val currency: String,
    val description: String,
    val status: String,
    val cardId: String,
    val createdAt: String
)

data class TransactionMeta(
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val totalPages: Int
)

data class TransactionHistoryResponse(
    override val success: Boolean,
    val data: List<TransactionData>?,
    val meta: TransactionMeta?,
    override val error: ApiError? = null
) : ApiResponse
