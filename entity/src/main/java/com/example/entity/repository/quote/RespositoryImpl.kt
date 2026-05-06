package com.example.entity.repository.quote

import com.example.entity.Quote
import com.example.entity.local.QuoteDao
import com.example.entity.local.QuoteEntity
import com.example.entity.network.QuoteApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(
    private val quoteApi: QuoteApi,
    private val quoteDao: QuoteDao
) : QuoteRepository {
    override fun getLocalNotes(): Flow<List<Quote>> = quoteDao.getAllQuotes().map { entity ->
        entity.map {
            it.toQuote()
        }
    }

    override suspend fun fetchAndSaveNewQuote() {

    }

//    override suspend fun fetchAndSaveNewQuote() {
//        val response = quoteApi.getQuote()
//        quoteDao.addQuote(QuoteEntity(0, response.author, response.quote))
//    }

    override suspend fun getLastQuote(): Quote? = quoteDao.getLastQuote()?.toQuote()
}

fun QuoteEntity.toQuote() = Quote(
    id = id,
    author = author,
    quote = quote
)