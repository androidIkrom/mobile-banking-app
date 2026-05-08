package com.example.usecase

import com.example.entity.model.auth.AuthData
import com.example.entity.model.card.CardData
import com.example.entity.model.transaction.TransactionData
import com.example.entity.model.user.UserProfile
import com.example.usecase.model.NetworkLog
import kotlinx.coroutines.flow.Flow


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

interface GetNetworkLogsUseCase{
    operator fun invoke(): Flow<List<NetworkLog>>
}
interface ClearLogsUseCase{
    suspend operator fun invoke()
}

interface GetCardsUseCase {
    suspend operator fun invoke(): Result<List<CardData>>
}

interface AttachCardUseCase {
    suspend operator fun invoke(cardNumber: String): Result<CardData>
}

interface GetTransactionsUseCase {
    suspend operator fun invoke(
        page: Int = 1,
        pageSize: Int = 20,
        cardId: String? = null,
        type: String? = null
    ): Result<List<TransactionData>>
}