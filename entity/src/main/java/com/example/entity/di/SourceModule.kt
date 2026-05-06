package com.example.entity.di

import android.content.Context
import androidx.room.Room
import com.example.entity.local.QuoteDao
import com.example.entity.local.QuoteDatabase
import com.example.entity.network.AuthApiService
import com.example.entity.network.AuthInterceptor
import com.example.entity.network.QuoteApi
import com.example.entity.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
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

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(
//        loggingInterceptor: HttpLoggingInterceptor,
//        authInterceptor: AuthInterceptor
//    ): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(authInterceptor) // Add header first
//            .addInterceptor(loggingInterceptor) // Then log the request with headers
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient : OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl("http://173.212.244.180:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideQuoteApi(retrofit: Retrofit) : QuoteApi = retrofit.create(QuoteApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) : AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : UserApiService = retrofit.create(UserApiService::class.java)
}
//jhjjhjbjbhjbjbbjhhhhhhhhhhbjhjhbbjkjkkkkkkkjioijkjkttgnjnjk;kjn