package com.example.entity.repository.transfer

import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.model.transfer.ConfirmOtpRequest
import com.example.entity.model.transfer.ConfirmPinRequest
import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponseData
import com.example.entity.network.transfer.TransferApiService
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor(
    private val api: TransferApiService
) : TransferRepository {
    override suspend fun initiateTransfer(request: InitiateTransferRequest): Result<TransferResponseData> {
        return try {
            val response = api.initiateTransfer(request)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.error?.message ?: "O`tkazma boshlanmadi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmWithPin(
        token: String,
        pin: String
    ): Result<ConfirmTransferResponseData> {
        return try {
            val response = api.confirmWithPin(ConfirmPinRequest(token, pin))

            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.error?.message ?: "Pin tasdiqlanmadi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmWitOtp(
        token: String,
        otp: String
    ): Result<ConfirmTransferResponseData> {
        return try {
            val response = api.confirmWithOtp(ConfirmOtpRequest(token, otp))
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.error?.message ?: "OTP tasdiqlanmadi"))

            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkCard(cardNumber: String): Result<CheckCardResponseData> {
        return try {
            val response = api.checkCard(cardNumber)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.error?.message?: "Bunday card topilmadi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
