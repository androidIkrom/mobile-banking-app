package com.example.entity.repository.payment

import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest
import com.example.entity.network.payment.PaymentService
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api : PaymentService
) : PaymentRepository{
    override suspend fun getProviders(category: String?): Result<List<PaymentProvider>> = try{
        val response = api.getProviders(category)
        val body = response.body()
        if (response.isSuccessful && body?.success == true && body.data != null){
            Result.success(body.data)
        }
        else{
            Result.failure(Exception(body?.error?.message ?: "Failed to get"))
        }
    }
    catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun makePayment(request: PaymentRequest): Result<String> = try {
        val response = api.makePayment(request)
        if (response.isSuccessful && response.body()?.success == true)
        Result.success("To`lov to`landi")
        else Result.failure(Exception(response.body()?.error?.message ?:"to`lov o`tkazilmadi"))
    }
    catch (e : Exception){
        Result.failure(e)
    }
}