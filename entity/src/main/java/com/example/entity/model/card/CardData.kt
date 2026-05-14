package com.example.entity.model.card

data class CardData(
    val id: String,
    val maskedNumber: String,
    val holderName: String,
    val expiry: String,
    val balance: Double,
    val currency: String,
    val isMain: Boolean,
    val isBlocked: Boolean,
    val type: String, // UZCARD, HUMO, etc.
    val backgroundUrl: String? = null
)

data class AttachCardRequest(
    val cardNumber: String
)