package com.example.usecase.impl.auth

import com.example.entity.model.auth.AuthData
import com.example.entity.repository.auth.AuthRepository
import com.example.usecase.RefreshTokenUseCase
import javax.inject.Inject

class RefreshTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RefreshTokenUseCase {
    override suspend fun invoke(refreshToken: String): Result<AuthData> {
        return authRepository.refreshToken(refreshToken)
    }
}