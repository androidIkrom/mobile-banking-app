package com.example.entity.repository.card

import com.example.entity.model.card.CardData

interface CardRepository {
    suspend fun getCards(): Result<List<CardData>>
    suspend fun attachCard(cardNumber: String): Result<CardData>
}