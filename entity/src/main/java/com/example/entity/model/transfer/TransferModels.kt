package com.example.entity.model.transfer

import com.example.entity.model.auth.ApiError
import com.example.entity.model.auth.ApiResponse

data class InitiateTransferRequest(
    val fromCardId : String,
    val toCardNumber : String,
    val amount : Double,
    val pin : String,
    val description : String
)

data class TransferResponseData(
    val requiresConfirmation :  Boolean,
    val confirmToken : String,
    val status : String
)

data class TransferResponse(
    override val success : Boolean,
    val data  :  TransferResponseData?,
    override val error : ApiError?
) : ApiResponse


data class ConfirmPinRequest(
    val confirmToken : String,
    val pin : String
)

data class ConfirmOtpRequest(
    val confirmToken : String,
    val otp : String
)

data class ConfirmTransferResponseData(
    val transactionId : String,
    val status : String,
    val amount: Double
)

data class ConfirmTransferResponse(
    override val success : Boolean,
    val data  : ConfirmTransferResponseData?,
    override val error : ApiError?
) : ApiResponse
