package com.example.entity.network.loan

import com.example.entity.model.loan.ApplyLoanRequest
import com.example.entity.model.loan.LoanResponse
import com.example.entity.model.loan.LoanSingleResponse
import com.example.entity.model.loan.RepayLoanRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoanApiService {
    @GET("/api/v1/loans")
    suspend fun getLoans(): Response<LoanResponse>

    @POST("/api/v1/loans")
    suspend fun applyLoan(
        @Body request: ApplyLoanRequest
    ): Response<LoanSingleResponse>

    @POST("/api/v1/loans/{id}/repay")
    suspend fun repayLoan(
        @Path("id") id: String,
        @Body request: RepayLoanRequest
    ): Response<LoanSingleResponse>
}
