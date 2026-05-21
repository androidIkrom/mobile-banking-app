package com.example.entity.di

import com.example.entity.repository.auth.AuthRepository
import com.example.entity.repository.auth.AuthRepositoryImpl
import com.example.entity.repository.card.CardRepository
import com.example.entity.repository.card.CardRepositoryImpl
import com.example.entity.repository.intercepter.InterceptorRepository
import com.example.entity.repository.intercepter.InterceptorRepositoryImpl
import com.example.entity.repository.loan.LoanRepository
import com.example.entity.repository.loan.LoanRepositoryImpl
import com.example.entity.repository.payment.PaymentRepository
import com.example.entity.repository.payment.PaymentRepositoryImpl
import com.example.entity.repository.transfer.TransferRepository
import com.example.entity.repository.transfer.TransferRepositoryImpl
import com.example.entity.repository.user.UserRepository
import com.example.entity.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindInterceptorRepository(interceptorRepositoryImpl: InterceptorRepositoryImpl): InterceptorRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindTransferRepository(transferRepositoryImpl: TransferRepositoryImpl): TransferRepository

    @Binds
    fun bindPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    fun bindLoanRepository(loanRepositoryImpl: LoanRepositoryImpl): LoanRepository

    @Binds
    fun bindExchangeRepository(impl: com.example.entity.repository.exchange.ExchangeRepositoryImpl): com.example.entity.repository.exchange.ExchangeRepository

    @Binds
    fun bindKycRepository(impl: com.example.entity.repository.kyc.KycRepositoryImpl): com.example.entity.repository.kyc.KycRepository
}
