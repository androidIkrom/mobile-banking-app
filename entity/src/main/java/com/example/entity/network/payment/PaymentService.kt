package com.example.entity.network.payment

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentService {
    @GET("api/v1/payments/providers")
    suspend fun getProviders(
        @Query("category")
        category: String? = null
    ): Response<BaseResponse<List<PaymentProvider>>>
    @POST("api/v1/payments")
    suspend fun makePayment(
        @Body paymentRequest: PaymentRequest
    ) : Response<BaseResponse<Unit>>
}