package com.example.entity.model.transfer

import com.example.entity.model.auth.ApiError

data class InitiateTransferRequest(
    val fromCardId : String,
    val toCardNumber : String,
    val amount : Long,
    val pin : String,
    val description : String
)

data class TransferResponseData(
    val requiresConfirmation :  Boolean,
    val confirmToken : String,
    val status : String
)

data class TransferResponse(
    val success : Boolean,
    val data  :  TransferResponseData?,
    val error : ApiError?
)


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
    val amount: Long
)

data class ConfirmTransferResponse(
    val success : Boolean,
    val data  : ConfirmTransferResponseData?,
    val error : ApiError?
)