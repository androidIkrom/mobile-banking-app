package com.example.usecase.impl

import com.example.entity.repository.user.UserRepository
import com.example.entity.model.user.UserProfile
import com.example.usecase.GetProfileUseCase
import javax.inject.Inject

internal class GetProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetProfileUseCase {
    override suspend fun invoke(): Result<UserProfile> = userRepository.getProfile()
}
