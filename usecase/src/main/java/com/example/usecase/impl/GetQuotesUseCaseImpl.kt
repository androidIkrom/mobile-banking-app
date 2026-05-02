package com.example.usecase.impl

import com.example.entity.QuoteRepository
import com.example.usecase.GetLocalNoteUseCase
import javax.inject.Inject

internal class GetQuotesUseCaseImpl @Inject constructor(
    private val repository: QuoteRepository
) : GetLocalNoteUseCase {
    override suspend operator fun invoke() = repository.getLocalNotes()
}
//jkgfgfgfgfggfgf