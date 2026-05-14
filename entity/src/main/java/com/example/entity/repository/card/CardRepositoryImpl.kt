package com.example.entity.repository.card

import com.example.entity.model.card.AttachCardRequest
import com.example.entity.model.card.CardData
import com.example.entity.network.card.CardApiService
import com.example.entity.network.toResult
import javax.inject.Inject

internal class CardRepositoryImpl @Inject constructor(
    private val api: CardApiService
) : CardRepository {
    override suspend fun getCards(): Result<List<CardData>> = api.getCards().toResult()

    override suspend fun attachCard(cardNumber: String): Result<CardData> = 
        api.attachCard(AttachCardRequest(cardNumber)).toResult()
}
