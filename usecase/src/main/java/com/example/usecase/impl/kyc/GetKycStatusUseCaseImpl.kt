package com.example.usecase.impl.kyc

import com.example.entity.model.kyc.KycStatusData
import com.example.entity.repository.kyc.KycRepository
import com.example.usecase.GetKycStatusUseCase
import javax.inject.Inject

class GetKycStatusUseCaseImpl @Inject constructor(
    private val kycRepository: KycRepository
) : GetKycStatusUseCase {
    override suspend fun invoke(): Result<KycStatusData> {
        return kycRepository.getKycStatus()
    }
}
