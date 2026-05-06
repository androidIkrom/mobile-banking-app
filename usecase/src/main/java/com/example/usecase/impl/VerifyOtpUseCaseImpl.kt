package com.example.usecase.impl

import com.example.entity.repository.auth.AuthRepository
import com.example.entity.model.auth.AuthData
import com.example.usecase.VerifyOtpUseCase
import javax.inject.Inject

class VerifyOtpUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : VerifyOtpUseCase {
    override suspend fun invoke(phone: String, otp: String): Result<AuthData> {
        return authRepository.verifyOtp(phone, otp)
    }
}
