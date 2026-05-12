package com.example.usecase.impl.transfer

import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.repository.transfer.TransferRepository
import com.example.usecase.ConfirmOtpUseCase
import javax.inject.Inject

class ConfirmOtpUseCaseImpl @Inject constructor(
    private val repository: TransferRepository
) : ConfirmOtpUseCase {
    override suspend fun invoke(
        token: String,
        otp: String
    ): Result<ConfirmTransferResponseData> = repository.confirmWitOtp(token, otp)
}