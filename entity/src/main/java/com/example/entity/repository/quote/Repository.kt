package com.example.entity.repository.quote

import com.example.entity.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getLocalNotes(): Flow<List<Quote>>
    suspend fun fetchAndSaveNewQuote()
    suspend fun getLastQuote(): Quote?
}


