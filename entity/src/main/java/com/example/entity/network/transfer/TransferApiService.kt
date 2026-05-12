package com.example.entity.network.transfer

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.model.transfer.ConfirmOtpRequest
import com.example.entity.model.transfer.ConfirmPinRequest
import com.example.entity.model.transfer.ConfirmTransferResponse
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransferApiService {
    @POST("/api/v1/transfers")
    suspend fun initiateTransfer(
        @Body request: InitiateTransferRequest
    ): Response<TransferResponse>

    @POST("/api/v1/transfers/confirm-pin")
    suspend fun confirmWithPin(
        @Body reqConfirmPinRequest: ConfirmPinRequest
    ): Response<ConfirmTransferResponse>

    @POST("/api/v1/transfers/confirm-otp")
    suspend fun confirmWithOtp(
        @Body request: ConfirmOtpRequest
    ): Response<ConfirmTransferResponse>

    @GET("/api/v1/transfers/check-card")
    suspend fun checkCard(
        @Query("cardNumber") cardNumber : String
    ) : Response<BaseResponse<CheckCardResponseData>>
}