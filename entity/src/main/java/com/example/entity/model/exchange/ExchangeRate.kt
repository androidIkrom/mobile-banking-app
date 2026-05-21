package com.example.entity.model.exchange

data class ExchangeRate(
    val currency: String,
    val buy: Double,
    val sell: Double,
    val updatedAt: String
)