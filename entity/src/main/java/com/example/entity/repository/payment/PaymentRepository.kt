package com.example.entity.repository.payment

import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest

interface PaymentRepository {
    suspend fun getProviders(category: String? = null): Result<List<PaymentProvider>>
    suspend fun makePayment(request: PaymentRequest): Result<String>
}