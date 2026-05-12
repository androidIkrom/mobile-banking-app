package com.example.usecase.di

import com.example.entity.repository.payment.PaymentRepositoryImpl
import com.example.usecase.AttachCardUseCase
import com.example.usecase.CheckCardUseCase
import com.example.usecase.ClearLogsUseCase
import com.example.usecase.ConfirmOtpUseCase
import com.example.usecase.ConfirmPinUseCase
import com.example.usecase.GetCardsUseCase
import com.example.usecase.GetLoansUseCase
import com.example.usecase.GetNetworkLogsUseCase
import com.example.usecase.GetProfileUseCase
import com.example.usecase.GetProvidersUseCase
import com.example.usecase.GetTransactionsUseCase
import com.example.usecase.InitiateTransferUseCase
import com.example.usecase.MakePaymentUseCase
import com.example.usecase.RefreshTokenUseCase
import com.example.usecase.SendOtpUseCase
import com.example.usecase.SetPinUseCase
import com.example.usecase.UpdateProfileUseCase
import com.example.usecase.VerifyOtpUseCase
import com.example.usecase.impl.auth.RefreshTokenUseCaseImpl
import com.example.usecase.impl.auth.SendOtpUseCaseImpl
import com.example.usecase.impl.auth.SetPinUseCaseImpl
import com.example.usecase.impl.auth.VerifyOtpUseCaseImpl
import com.example.usecase.impl.loan.GetLoansUseCaseImpl
import com.example.usecase.impl.cards.AttachCardUseCaseImpl
import com.example.usecase.impl.cards.GetCardsUseCaseImpl
import com.example.usecase.impl.chucker.ClearLogsUseCaseImpl
import com.example.usecase.impl.chucker.GetAllLogsUseCaseImpl
import com.example.usecase.impl.payment.GetProvidersUseCaseImpl
import com.example.usecase.impl.payment.MakePaymentUseCaseImpl
import com.example.usecase.impl.transfer.CheckCardUseCaseImpl
import com.example.usecase.impl.transfer.ConfirmOtpUseCaseImpl
import com.example.usecase.impl.transfer.ConfirmPinUseCaseImpl
import com.example.usecase.impl.transfer.InitiateTransferUseCaseImpl
import com.example.usecase.impl.user.GetProfileUseCaseImpl
import com.example.usecase.impl.user.GetTransactionsUseCaseImpl
import com.example.usecase.impl.user.UpdateProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface UseCaseModule {

    @Binds
    fun bindGetCardsUseCase(impl: GetCardsUseCaseImpl): GetCardsUseCase

    @Binds
    fun bindAttachCardUseCase(impl: AttachCardUseCaseImpl): AttachCardUseCase

    @Binds
    fun bindGetTransactionsUseCase(impl: GetTransactionsUseCaseImpl): GetTransactionsUseCase

    @Binds
    fun bindGetAllLogsUseCase(getAllLogsUseCaseImpl: GetAllLogsUseCaseImpl): GetNetworkLogsUseCase

    @Binds
    fun bindClearAllLogsUseCase(clearLogsUseCaseImpl: ClearLogsUseCaseImpl): ClearLogsUseCase

    @Binds
    fun bindSendOtpUseCase(impl: SendOtpUseCaseImpl): SendOtpUseCase

    @Binds
    fun bindSetPinUseCase(impl: SetPinUseCaseImpl): SetPinUseCase

    @Binds
    fun bindVerifyOtpUseCase(impl: VerifyOtpUseCaseImpl): VerifyOtpUseCase

    @Binds
    fun bindRefreshTokenUseCase(impl: RefreshTokenUseCaseImpl): RefreshTokenUseCase

    @Binds
    fun bindGetProfileUseCase(impl: GetProfileUseCaseImpl): GetProfileUseCase

    @Binds
    fun bindUpdateProfileUseCase(impl: UpdateProfileUseCaseImpl): UpdateProfileUseCase

    @Binds
    fun bindInitiateTransferUseCase(
        impl: InitiateTransferUseCaseImpl
    ): InitiateTransferUseCase

    @Binds
    fun bindConfirmPinUseCase(
        impl: ConfirmPinUseCaseImpl
    ): ConfirmPinUseCase

    @Binds
    fun bindConfirmOtpUseCase(
        impl: ConfirmOtpUseCaseImpl
    ): ConfirmOtpUseCase

    @Binds
    fun bindCheckCardUseCase(
        impl: CheckCardUseCaseImpl
    ): CheckCardUseCase

    @Binds
    fun bindGetProvidersUseCase(
        impl: GetProvidersUseCaseImpl
    ): GetProvidersUseCase

    @Binds
    fun bindMakePaymentUseCase(
        impl: MakePaymentUseCaseImpl
    ): MakePaymentUseCase

    @Binds
    fun bindGetLoansUseCase(
        impl: GetLoansUseCaseImpl
    ): GetLoansUseCase
}
