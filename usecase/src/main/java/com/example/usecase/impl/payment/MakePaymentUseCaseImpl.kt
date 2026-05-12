package com.example.usecase.impl.payment

import com.example.entity.model.payment.PaymentRequest
import com.example.entity.repository.payment.PaymentRepository
import com.example.usecase.MakePaymentUseCase
import javax.inject.Inject

class MakePaymentUseCaseImpl @Inject constructor(
    private val repository: PaymentRepository
) : MakePaymentUseCase {
    override suspend fun invoke(request: PaymentRequest): Result<String> =
        repository.makePayment(request)
}