package com.example.entity.network

import com.example.entity.model.ApiResponse
import retrofit2.http.GET


interface QuoteApi {
    @GET("quotes/random")
    suspend fun getQuote(): ApiResponse
}