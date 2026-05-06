package com.example.entity.network

import com.example.entity.local.PrefsManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val prefsManager: PrefsManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        
        prefsManager.accessToken?.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
