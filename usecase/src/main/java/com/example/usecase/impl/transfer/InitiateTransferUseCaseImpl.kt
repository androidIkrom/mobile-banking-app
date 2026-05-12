package com.example.usecase.impl.transfer

import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponseData
import com.example.entity.repository.transfer.TransferRepository
import com.example.usecase.InitiateTransferUseCase
import javax.inject.Inject

class InitiateTransferUseCaseImpl @Inject constructor(
    private val repository : TransferRepository
) : InitiateTransferUseCase {
    override suspend fun invoke(request: InitiateTransferRequest): Result<TransferResponseData> = repository.initiateTransfer(request)
}