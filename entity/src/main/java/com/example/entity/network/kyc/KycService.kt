package com.example.entity.network.kyc

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.kyc.KycStatusData
import com.example.entity.model.kyc.KycSubmitRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface KycService {
    @GET("api/v1/kyc")
    suspend fun getKycStatus(): Response<BaseResponse<KycStatusData>>

    @POST("api/v1/kyc")
    suspend fun submitKyc(@Body request: KycSubmitRequest): Response<BaseResponse<Unit>>
}
