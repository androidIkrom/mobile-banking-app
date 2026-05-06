package com.example.entity.network

import com.example.entity.model.quote.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/v1/quotes/random")
    suspend fun getRandomQuote() : Response<ApiResponse>
}