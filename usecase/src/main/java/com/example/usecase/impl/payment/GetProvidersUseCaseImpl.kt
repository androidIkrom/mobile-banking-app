package com.example.usecase.impl.payment

import com.example.entity.model.payment.PaymentProvider
import com.example.entity.repository.payment.PaymentRepository
import com.example.usecase.GetProvidersUseCase
import javax.inject.Inject

class GetProvidersUseCaseImpl @Inject constructor(
    private val repository: PaymentRepository
) : GetProvidersUseCase{
    override suspend fun invoke(category: String?): Result<List<PaymentProvider>> = repository.getProviders(category)

}