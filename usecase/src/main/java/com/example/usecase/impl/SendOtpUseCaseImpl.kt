package com.example.usecase.impl

import com.example.entity.repository.auth.AuthRepository
import com.example.usecase.SendOtpUseCase
import javax.inject.Inject

class SendOtpUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SendOtpUseCase {
    override suspend fun invoke(phone: String): Result<Unit> {
        return authRepository.sendOtp(phone)
    }
}
