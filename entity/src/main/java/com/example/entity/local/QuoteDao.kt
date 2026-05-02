package com.example.entity.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    fun getAllQuotes() : Flow<List<QuoteEntity>>
    @Insert
    suspend fun addQuote(quoteEntity: QuoteEntity)
    @Query("SELECT * FROM quotes ORDER BY id DESC LIMIT 1")
    suspend fun getLastQuote() : QuoteEntity?
}