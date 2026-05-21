package com.example.entity.repository.kyc

import com.example.entity.model.kyc.KycStatusData

interface KycRepository {
    suspend fun getKycStatus(): Result<KycStatusData>
    suspend fun submitKyc(
        passportSeries: String,
        passportNumber: String,
        birthDate: String,
        selfieBase64: String
    ): Result<Unit>
}
