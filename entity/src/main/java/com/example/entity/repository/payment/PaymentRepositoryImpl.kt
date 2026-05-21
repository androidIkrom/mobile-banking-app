package com.example.entity.repository.payment

import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest
import com.example.entity.network.payment.PaymentService
import com.example.entity.network.toResult
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api : PaymentService
) : PaymentRepository{
    override suspend fun getProviders(category: String?): Result<List<PaymentProvider>> = try {
        api.getProviders(category).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun makePayment(request: PaymentRequest): Result<String> = try {
        api.makePayment(request).toResult().map { "To`lov to`landi" }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
