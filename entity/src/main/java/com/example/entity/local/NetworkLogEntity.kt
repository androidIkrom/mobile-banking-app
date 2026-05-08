package com.example.entity.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_logs")
data class NetworkLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0,
    val method : String,
    val url :  String,
    val code :  String,
    val requestBody : String?,
    val responseBody: String,
    val message : String,
    val timeStamp : Long,
    val duration : Long

)