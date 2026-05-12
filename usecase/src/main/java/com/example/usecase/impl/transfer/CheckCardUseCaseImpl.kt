package com.example.usecase.impl.transfer

import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.repository.transfer.TransferRepository
import com.example.usecase.CheckCardUseCase
import javax.inject.Inject

class CheckCardUseCaseImpl @Inject constructor(
    private val repository: TransferRepository
) : CheckCardUseCase {
    override suspend fun invoke(cardNumber: String): Result<CheckCardResponseData> =
        repository.checkCard(cardNumber)
}