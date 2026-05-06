package com.example.entity.di

import com.example.entity.repository.auth.AuthRepository
import com.example.entity.repository.auth.AuthRepositoryImpl
import com.example.entity.repository.quote.QuoteRepository
import com.example.entity.repository.quote.RepositoryImpl
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
    fun bindRepository(repositoryImpl: RepositoryImpl): QuoteRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
