package com.example.usecase

import com.example.entity.Quote
import com.example.entity.model.auth.AuthData
import com.example.entity.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface GetLocalNoteUseCase{
    suspend operator fun invoke() : Flow<List<Quote>>
}

interface FetchNewQuoteUseCase{
    suspend operator fun invoke()
}
interface GetLastQuoteUseCase{
    suspend operator fun invoke() : Quote?
}

interface SendOtpUseCase {
    suspend operator fun invoke(phone: String): Result<Unit>
}

interface VerifyOtpUseCase {
    suspend operator fun invoke(phone: String, otp: String): Result<AuthData>
}

interface RefreshTokenUseCase {
    suspend operator fun invoke(refreshToken: String): Result<AuthData>
}

interface GetProfileUseCase {
    suspend operator fun invoke(): Result<UserProfile>
}

interface UpdateProfileUseCase {
    suspend operator fun invoke(fullName: String): Result<UserProfile>
}
