package com.example.entity.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [QuoteEntity::class], version = 2, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase(){
    abstract fun getQuoteDao() : QuoteDao
}