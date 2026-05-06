package com.example.usecase.di

import com.example.usecase.*
import com.example.usecase.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface UseCaseModule {
    @Binds
    fun bindGetLocalNotesUseCase(impl: GetQuotesUseCaseImpl): GetLocalNoteUseCase

    @Binds
    fun bindFetchNewQuoteUseCase(impl: FetchNewQuoteUseCaseImpl): FetchNewQuoteUseCase

    @Binds
    fun bindGetLastQuoteUseCase(impl: GetLastQuoteUseCaseImpl): GetLastQuoteUseCase

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
