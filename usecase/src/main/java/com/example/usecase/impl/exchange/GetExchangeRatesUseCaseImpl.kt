package com.example.usecase.impl.exchange

import com.example.entity.model.exchange.ExchangeRate
import com.example.entity.repository.exchange.ExchangeRepository
import com.example.usecase.GetExchangeRatesUseCase
import javax.inject.Inject

internal class GetExchangeRatesUseCaseImpl @Inject constructor(
    private val repository: ExchangeRepository
) : GetExchangeRatesUseCase {
    override suspend fun invoke(): Result<List<ExchangeRate>> {
        return repository.getExchangeRates()
    }
}
