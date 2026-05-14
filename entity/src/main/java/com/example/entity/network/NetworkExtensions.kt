package com.example.entity.network

import com.example.entity.model.auth.ApiResponse
import com.example.entity.model.auth.BaseResponse
import com.google.gson.Gson
import retrofit2.Response

fun <T> Response<BaseResponse<T>>.toResult(): Result<T> {
    return try {
        val body = body()
        if (isSuccessful && body != null && body.success && body.data != null) {
            Result.success(body.data)
        } else if (isSuccessful && body != null && body.success && body.data == null) {
            @Suppress("UNCHECKED_CAST")
            Result.success(Unit as T)
        } else {
            val errorMsg = body?.error?.message ?: parseErrorBody() ?: "Noma'lum xatolik"
            Result.failure(Exception(errorMsg))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun <T : ApiResponse> Response<T>.toApiResult(): Result<T> {
    return try {
        val body = body()
        if (isSuccessful && body != null && body.success) {
            Result.success(body)
        } else {
            val errorMsg = body?.error?.message ?: parseErrorBody() ?: "Noma'lum xatolik"
            Result.failure(Exception(errorMsg))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private fun <T> Response<T>.parseErrorBody(): String? {
    return try {
        val errorJson = errorBody()?.string()
        val gson = Gson()
        val errorResponse = gson.fromJson(errorJson, BaseResponse::class.java)
        errorResponse?.error?.message
    } catch (e: Exception) {
        null
    }
}
