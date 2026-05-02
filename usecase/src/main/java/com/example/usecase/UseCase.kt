package com.example.usecase

import com.example.entity.Quote
import kotlinx.coroutines.flow.Flow

interface GetLocalNoteUseCase{
    suspend operator fun invoke() : Flow<List<Quote>>
}

interface FetchNewQuoteUseCase{
    suspend operator fun invoke()
}
interface GetLastQuoteUseCase{
    suspend operator fun invoke() : Quote?
}


