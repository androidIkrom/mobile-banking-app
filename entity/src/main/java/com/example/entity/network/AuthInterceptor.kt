package com.example.entity.network

import com.example.entity.local.PrefsManager
import com.example.entity.model.auth.RefreshTokenRequest
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val prefsManager: PrefsManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        prefsManager.accessToken?.let { token ->
            request.header("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}

@Singleton
class TokenAuthenticator @Inject constructor(
    private val prefsManager: PrefsManager, 
    private val authApiService: Lazy<AuthApiService>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = prefsManager.refreshToken ?: return null
        synchronized(this) {
            val currentToken = prefsManager.accessToken
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            
            if (currentToken != requestToken && currentToken != null) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }
            
            return try {
                val refreshResponse = runBlocking {
                    authApiService.get().refreshToken(RefreshTokenRequest(refreshToken = refreshToken))
                }
                
                if (refreshResponse.isSuccessful && refreshResponse.body()?.success == true) {
                    val newData = refreshResponse.body()?.data
                    if (newData != null) {
                        prefsManager.accessToken = newData.accessToken
                        prefsManager.refreshToken = newData.refreshToken
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${newData.accessToken}")
                            .build()
                    } else null
                } else {
                    prefsManager.clear()
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
//dffddf