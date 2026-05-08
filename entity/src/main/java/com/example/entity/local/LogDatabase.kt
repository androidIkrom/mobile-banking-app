package com.example.entity.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [NetworkLogEntity::class], version = 3, exportSchema = false)
abstract class LogDatabase : RoomDatabase(){
    abstract fun getQuoteDao() : NetworkLogDao
}