package com.example.entity.repository.card

import com.example.entity.model.card.AttachCardRequest
import com.example.entity.model.card.CardData
import com.example.entity.network.CardApiService
import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor(
    private val api: CardApiService
) : CardRepository {
    override suspend fun getCards(): Result<List<CardData>> = try {
        val response = api.getCards()
        val body = response.body()
        if (response.isSuccessful && body?.success == true && body.data != null) {
            Result.success(body.data)
        } else Result.failure(Exception(body?.error?.message ?: "Failed"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun attachCard(cardNumber: String): Result<CardData> = try {
        val response = api.attachCard(AttachCardRequest(cardNumber))
        val body = response.body()
        if (response.isSuccessful && body?.success == true && body.data != null) {
            Result.success(body.data)
        } else Result.failure(Exception(body?.error?.message ?: "Failed"))
    } catch (e: Exception) { Result.failure(e) }
}