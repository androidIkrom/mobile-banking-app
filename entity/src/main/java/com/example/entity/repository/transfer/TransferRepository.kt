package com.example.entity.repository.transfer

import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponseData

interface TransferRepository {
    suspend fun initiateTransfer(request: InitiateTransferRequest) : Result<TransferResponseData>
    suspend fun confirmWithPin(token : String,pin : String): Result<ConfirmTransferResponseData>
    suspend fun confirmWitOtp(token : String,otp : String): Result<ConfirmTransferResponseData>
    suspend fun checkCard(cardNumber : String): Result<CheckCardResponseData>
}