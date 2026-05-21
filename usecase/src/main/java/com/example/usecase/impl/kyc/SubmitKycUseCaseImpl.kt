package com.example.usecase.impl.kyc

import com.example.entity.repository.kyc.KycRepository
import com.example.usecase.SubmitKycUseCase
import javax.inject.Inject

class SubmitKycUseCaseImpl @Inject constructor(
    private val kycRepository: KycRepository
) : SubmitKycUseCase {
    override suspend fun invoke(
        passportSeries: String,
        passportNumber: String,
        birthDate: String,
        selfieBase64: String
    ): Result<Unit> {
        return kycRepository.submitKyc(
            passportSeries = passportSeries,
            passportNumber = passportNumber,
            birthDate = birthDate,
            selfieBase64 = selfieBase64
        )
    }
}
