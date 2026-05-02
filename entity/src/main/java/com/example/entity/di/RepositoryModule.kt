package com.example.entity.di

import android.content.Context
import androidx.room.Room
import com.example.entity.QuoteRepository
import com.example.entity.RepositoryImpl
import com.example.entity.local.QuoteDao
import com.example.entity.local.QuoteDatabase
import com.example.entity.network.QuoteApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl): QuoteRepository

}
