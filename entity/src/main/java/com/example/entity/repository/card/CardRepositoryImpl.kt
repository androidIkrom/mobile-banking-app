package com.example.entity.repository.card

import com.example.entity.model.card.AttachCardRequest
import com.example.entity.model.card.CardData
import com.example.entity.network.card.CardApiService
import com.example.entity.network.toResult
import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor(
    private val api: CardApiService
) : CardRepository {
    override suspend fun getCards(): Result<List<CardData>> = try {
        api.getCards().toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun attachCard(cardNumber: String): Result<CardData> = try {
        api.attachCard(AttachCardRequest(cardNumber)).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun setMainCard(cardId: String): Result<Unit> = try {
        api.setMainCard(cardId).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
