package com.example.usecase.impl

import com.example.entity.Quote
import com.example.entity.repository.quote.QuoteRepository
import com.example.usecase.GetLastQuoteUseCase
import javax.inject.Inject

class GetLastQuoteUseCaseImpl @Inject constructor(
    private val repository: QuoteRepository
) : GetLastQuoteUseCase {
    override suspend fun invoke(): Quote? {
        val lastQuote = repository.getLastQuote()
        return lastQuote
    }
}