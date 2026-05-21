package com.example.entity.repository.transfer

import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.model.transfer.ConfirmOtpRequest
import com.example.entity.model.transfer.ConfirmPinRequest
import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponseData
import com.example.entity.network.transfer.TransferApiService
import com.example.entity.network.toApiResult
import com.example.entity.network.toResult
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor(
    private val api: TransferApiService
) : TransferRepository {
    override suspend fun initiateTransfer(request: InitiateTransferRequest): Result<TransferResponseData> = try {
        api.initiateTransfer(request).toApiResult().mapCatching { 
            it.data ?: throw Exception("O'tkazma ma'lumoti topilmadi")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun confirmWithPin(
        token: String,
        pin: String
    ): Result<ConfirmTransferResponseData> = try {
        api.confirmWithPin(ConfirmPinRequest(token, pin)).toApiResult().mapCatching { 
            it.data ?: throw Exception("Tasdiqlash ma'lumoti topilmadi")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun confirmWitOtp(
        token: String,
        otp: String
    ): Result<ConfirmTransferResponseData> = try {
        api.confirmWithOtp(ConfirmOtpRequest(token, otp)).toApiResult().mapCatching { 
            it.data ?: throw Exception("Tasdiqlash ma'lumoti topilmadi")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkCard(cardNumber: String): Result<CheckCardResponseData> = try {
        api.checkCard(cardNumber).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
