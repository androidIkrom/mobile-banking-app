package com.example.entity.di

import android.content.Context
import androidx.room.Room
import com.example.entity.local.QuoteDao
import com.example.entity.local.QuoteDatabase
import com.example.entity.network.QuoteApi
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

internal class SourceModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context): QuoteDatabase {
        return Room.databaseBuilder(
            context = context, QuoteDatabase::class.java,name =  "quotes.db"
        ).build()
    }
    @Provides
    fun provideQuoteDao(db : QuoteDatabase): QuoteDao = db.getQuoteDao()

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com/").addConverterFactory(
        GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideQuoteApi(retrofit: Retrofit) : QuoteApi = retrofit.create(QuoteApi::class.java)

}