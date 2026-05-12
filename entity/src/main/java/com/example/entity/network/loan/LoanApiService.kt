package com.example.entity.network.loan

import com.example.entity.model.loan.LoanResponse
import retrofit2.http.GET

interface LoanApiService {
    @GET("/api/v1/loans")
    suspend fun getLoans(): LoanResponse
}
