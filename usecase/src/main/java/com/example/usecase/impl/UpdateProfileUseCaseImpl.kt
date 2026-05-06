package com.example.usecase.impl

import com.example.entity.repository.user.UserRepository
import com.example.entity.model.user.UserProfile
import com.example.usecase.UpdateProfileUseCase
import javax.inject.Inject

internal class UpdateProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UpdateProfileUseCase {
    override suspend fun invoke(fullName: String): Result<UserProfile> = userRepository.updateProfile(fullName)
}
