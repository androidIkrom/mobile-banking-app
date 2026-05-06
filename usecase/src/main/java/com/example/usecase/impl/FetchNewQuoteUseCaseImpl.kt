package com.example.usecase.impl

import com.example.entity.repository.quote.QuoteRepository
import com.example.usecase.FetchNewQuoteUseCase
import javax.inject.Inject

internal class FetchNewQuoteUseCaseImpl @Inject constructor(
    private val repository: QuoteRepository

) : FetchNewQuoteUseCase {
    override suspend operator fun invoke() = repository.fetchAndSaveNewQuote(
) }

//jkjkjkjjmkjkjnkjjnjnnknknknkkmnkknnknk