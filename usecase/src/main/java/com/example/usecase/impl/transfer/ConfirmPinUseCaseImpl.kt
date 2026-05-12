package com.example.usecase.impl.transfer

import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.repository.transfer.TransferRepository
import com.example.usecase.ConfirmPinUseCase
import javax.inject.Inject

class ConfirmPinUseCaseImpl @Inject constructor(
    private val repository: TransferRepository
) : ConfirmPinUseCase {
    override suspend fun invoke(
        token: String,
        pin: String
    ): Result<ConfirmTransferResponseData> = repository.confirmWithPin(token, pin)
}