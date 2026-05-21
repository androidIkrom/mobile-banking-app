package com.example.entity.di

import android.content.Context
import androidx.room.Room
import com.example.entity.local.LogDatabase
import com.example.entity.local.NetworkLogDao
import com.example.entity.network.auth.AuthApiService
import com.example.entity.network.auth.AuthInterceptor
import com.example.entity.network.auth.TokenAuthenticator
import com.example.entity.network.card.CardApiService
import com.example.entity.network.loan.LoanApiService
import com.example.entity.network.payment.PaymentService
import com.example.entity.network.transfer.TransferApiService
import com.example.entity.network.user.NetworkLogInterceptor
import com.example.entity.network.user.UserApiService
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

    @Provides
    @Singleton
    fun provideTransferApi(retrofit: Retrofit): TransferApiService = retrofit.create(
        TransferApiService::class.java)

    @Singleton
    @Provides
    fun providePaymentApi(retrofit: Retrofit) : PaymentService = retrofit.create(PaymentService::class.java)

    @Provides
    @Singleton
    fun provideLoanApi(retrofit: Retrofit): LoanApiService = retrofit.create(LoanApiService::class.java)

    @Provides
    @Singleton
    fun provideExchangeApi(retrofit: Retrofit): com.example.entity.network.exchange.ExchangeApiService =
        retrofit.create(com.example.entity.network.exchange.ExchangeApiService::class.java)

    @Provides
    @Singleton
    fun provideKycApi(retrofit: Retrofit): com.example.entity.network.kyc.KycService =
        retrofit.create(com.example.entity.network.kyc.KycService::class.java)

}