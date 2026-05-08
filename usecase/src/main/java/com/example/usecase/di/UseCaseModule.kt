package com.example.usecase.di

import com.example.usecase.AttachCardUseCase
import com.example.usecase.ClearLogsUseCase
import com.example.usecase.GetCardsUseCase
import com.example.usecase.GetNetworkLogsUseCase
import com.example.usecase.GetProfileUseCase
import com.example.usecase.GetTransactionsUseCase
import com.example.usecase.RefreshTokenUseCase
import com.example.usecase.SendOtpUseCase
import com.example.usecase.UpdateProfileUseCase
import com.example.usecase.VerifyOtpUseCase
import com.example.usecase.impl.auth.RefreshTokenUseCaseImpl
import com.example.usecase.impl.auth.SendOtpUseCaseImpl
import com.example.usecase.impl.auth.VerifyOtpUseCaseImpl
import com.example.usecase.impl.cards.AttachCardUseCaseImpl
import com.example.usecase.impl.cards.GetCardsUseCaseImpl
import com.example.usecase.impl.chucker.ClearLogsUseCaseImpl
import com.example.usecase.impl.chucker.GetAllLogsUseCaseImpl
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
    fun bindVerifyOtpUseCase(impl: VerifyOtpUseCaseImpl): VerifyOtpUseCase

    @Binds
    fun bindRefreshTokenUseCase(impl: RefreshTokenUseCaseImpl): RefreshTokenUseCase

    @Binds
    fun bindGetProfileUseCase(impl: GetProfileUseCaseImpl): GetProfileUseCase

    @Binds
    fun bindUpdateProfileUseCase(impl: UpdateProfileUseCaseImpl): UpdateProfileUseCase
}
