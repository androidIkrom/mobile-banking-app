package com.example.entity.repository.exchange

import com.example.entity.model.exchange.ExchangeRate

interface ExchangeRepository {
    suspend fun getExchangeRates(): Result<List<ExchangeRate>>
}
