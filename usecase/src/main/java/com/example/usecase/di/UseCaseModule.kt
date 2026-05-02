package com.example.usecase.di

import com.example.usecase.FetchNewQuoteUseCase
import com.example.usecase.GetLastQuoteUseCase
import com.example.usecase.GetLocalNoteUseCase
import com.example.usecase.impl.FetchNewQuoteUseCaseImpl
import com.example.usecase.impl.GetLastQuoteUseCaseImpl
import com.example.usecase.impl.GetQuotesUseCaseImpl
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

}