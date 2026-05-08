package com.example.usecase.impl.user

import com.example.entity.repository.user.UserRepository
import com.example.usecase.GetTransactionsUseCase
import javax.inject.Inject

class GetTransactionsUseCaseImpl @Inject constructor(private val repo: UserRepository) :
    GetTransactionsUseCase {
    override suspend fun invoke(page: Int, pageSize: Int, cardId: String?, type: String?) =
        repo.getTransactionHistory(page, pageSize, cardId, type)
}