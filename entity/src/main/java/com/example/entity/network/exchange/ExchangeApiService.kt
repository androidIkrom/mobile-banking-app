package com.example.entity.network.exchange

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.exchange.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeApiService {
    @GET("/api/v1/exchange/rates")
    suspend fun getExchangeRates(): Response<BaseResponse<List<ExchangeRate>>>
}
