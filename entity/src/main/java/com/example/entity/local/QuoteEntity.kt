package com.example.entity.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val author : String,
    val quote : String
)