package com.example.usecase.model

data class NetworkLog(
    val id: Int = 0,
    val method: String,
    val url: String,
    val code: String,
    val requestBody: String?,
    val responseBody: String,
    val message: String,
    val timeStamp: Long,
    val duration: Long
)