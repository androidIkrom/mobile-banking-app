package com.example.usecase.impl.auth

import com.example.entity.repository.auth.AuthRepository
import com.example.usecase.SetPinUseCase
import javax.inject.Inject

class SetPinUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : SetPinUseCase {
    override suspend fun invoke(pin: String): Result<Unit> = repository.setPin(pin)
}
