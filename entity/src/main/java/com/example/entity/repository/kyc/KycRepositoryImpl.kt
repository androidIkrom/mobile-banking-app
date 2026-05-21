package com.example.entity.repository.kyc

import com.example.entity.model.kyc.KycStatusData
import com.example.entity.model.kyc.KycSubmitRequest
import com.example.entity.network.kyc.KycService
import com.example.entity.network.toResult
import javax.inject.Inject

class KycRepositoryImpl @Inject constructor(
    private val kycService: KycService
) : KycRepository {
    override suspend fun getKycStatus(): Result<KycStatusData> = try {
        kycService.getKycStatus().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun submitKyc(
        passportSeries: String,
        passportNumber: String,
        birthDate: String,
        selfieBase64: String
    ): Result<Unit> = try {
        kycService.submitKyc(
            KycSubmitRequest(
                passportSeries = passportSeries,
                passportNumber = passportNumber,
                birthDate = birthDate,
                selfieBase64 = selfieBase64
            )
        ).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
