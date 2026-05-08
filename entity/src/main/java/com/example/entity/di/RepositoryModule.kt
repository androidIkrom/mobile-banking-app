package com.example.entity.di

import com.example.entity.repository.auth.AuthRepository
import com.example.entity.repository.auth.AuthRepositoryImpl
import com.example.entity.repository.card.CardRepository
import com.example.entity.repository.card.CardRepositoryImpl
import com.example.entity.repository.intercepter.InterceptorRepository
import com.example.entity.repository.intercepter.InterceptorRepositoryImpl
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
}
