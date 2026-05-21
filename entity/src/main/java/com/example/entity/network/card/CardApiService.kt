package com.example.entity.network.card

import com.example.entity.model.auth.BaseResponse
import com.example.entity.model.card.AttachCardRequest
import com.example.entity.model.card.CardData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CardApiService {

    @GET("/api/v1/cards")
    suspend fun getCards(): Response<BaseResponse<List<CardData>>>

    @POST("/api/v1/cards/attach")
    suspend fun attachCard(
        @Body request: AttachCardRequest
    ): Response<BaseResponse<CardData>>

    @POST("/api/v1/cards/{id}/main")
    suspend fun setMainCard(
        @retrofit2.http.Path("id") id: String
    ): Response<BaseResponse<Unit>>
}