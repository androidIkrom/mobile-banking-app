package com.example.entity.di

import android.content.Context
import androidx.room.Room
import com.example.entity.local.LogDatabase
import com.example.entity.local.NetworkLogDao
import com.example.entity.network.AuthApiService
import com.example.entity.network.AuthInterceptor
import com.example.entity.network.CardApiService
import com.example.entity.network.NetworkLogInterceptor
import com.example.entity.network.TokenAuthenticator
import com.example.entity.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SourceModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LogDatabase {
        return Room.databaseBuilder(
            context = context, LogDatabase::class.java, name = "quotes.db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        networkLogInterceptor: NetworkLogInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkLogInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkLogDao(database: LogDatabase): NetworkLogDao {
        return database.getQuoteDao()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://banking-api.zokirov-mob-dev.uz/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideCardApi(retrofit: Retrofit): CardApiService =
        retrofit.create(CardApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)


}