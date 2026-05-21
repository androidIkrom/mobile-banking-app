package com.example.entity.repository.exchange

import com.example.entity.model.exchange.ExchangeRate
import com.example.entity.network.exchange.ExchangeApiService
import com.example.entity.network.toResult
import javax.inject.Inject

internal class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeApiService
) : ExchangeRepository {
    override suspend fun getExchangeRates(): Result<List<ExchangeRate>> = try {
        api.getExchangeRates().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
