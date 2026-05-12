package com.example.entity.model.payment

data class PaymentProvider(
    val id: String,
    val name: String,
    val category: String,
    val logoUrl: String?
)

data class PaymentRequest(
    val providerId : String,
    val cardId : String,
    val amount : Int,
    val account : String
)