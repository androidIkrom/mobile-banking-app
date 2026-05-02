package com.example.entity

import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getLocalNotes(): Flow<List<Quote>>
    suspend fun fetchAndSaveNewQuote()
    suspend fun getLastQuote(): Quote?
}


