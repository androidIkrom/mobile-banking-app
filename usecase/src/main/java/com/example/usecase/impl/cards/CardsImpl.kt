package com.example.usecase.impl.cards

import com.example.entity.repository.card.CardRepository
import com.example.usecase.AttachCardUseCase
import com.example.usecase.GetCardsUseCase
import javax.inject.Inject

class GetCardsUseCaseImpl @Inject constructor(private val repo: CardRepository) : GetCardsUseCase {
    override suspend fun invoke() = repo.getCards()
}

class AttachCardUseCaseImpl @Inject constructor(private val repo: CardRepository) :
    AttachCardUseCase {
    override suspend fun invoke(cardNumber: String) = repo.attachCard(cardNumber)
}