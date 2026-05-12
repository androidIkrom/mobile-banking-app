package com.example.usecase

import com.example.entity.model.auth.AuthData
import com.example.entity.model.card.CardData
import com.example.entity.model.loan.LoanData
import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest
import com.example.entity.model.transaction.TransactionData
import com.example.entity.model.transfer.CheckCardResponseData
import com.example.entity.model.transfer.ConfirmTransferResponseData
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.entity.model.transfer.TransferResponseData
import com.example.entity.model.user.UserProfile
import com.example.usecase.model.NetworkLog
import kotlinx.coroutines.flow.Flow


interface SendOtpUseCase {
    suspend operator fun invoke(phone: String): Result<Unit>
}

interface VerifyOtpUseCase {
    suspend operator fun invoke(phone: String, otp: String): Result<AuthData>
}

interface SetPinUseCase {
    suspend operator fun invoke(pin: String): Result<Unit>
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

interface GetNetworkLogsUseCase {
    operator fun invoke(): Flow<List<NetworkLog>>
}

interface ClearLogsUseCase {
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

interface InitiateTransferUseCase {
    suspend operator fun invoke(request: InitiateTransferRequest): Result<TransferResponseData>
}

interface ConfirmPinUseCase {
    suspend operator fun invoke(token: String, pin: String): Result<ConfirmTransferResponseData>
}

interface ConfirmOtpUseCase {
    suspend operator fun invoke(token: String, otp: String): Result<ConfirmTransferResponseData>
}

interface CheckCardUseCase {
    suspend operator fun invoke(cardNumber: String): Result<CheckCardResponseData>
}

interface GetProvidersUseCase {
    suspend operator fun invoke(category: String? = null): Result<List<PaymentProvider>>
}

interface MakePaymentUseCase {
    suspend operator fun invoke(request: PaymentRequest): Result<String>
}

interface GetLoansUseCase {
    suspend operator fun invoke(): Result<List<LoanData>>
}