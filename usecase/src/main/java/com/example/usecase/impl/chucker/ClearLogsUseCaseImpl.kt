package com.example.usecase.impl.chucker

import com.example.entity.repository.intercepter.InterceptorRepository
import com.example.usecase.ClearLogsUseCase
import javax.inject.Inject

internal class ClearLogsUseCaseImpl @Inject constructor(
    private val repository: InterceptorRepository
) : ClearLogsUseCase {
    override suspend fun invoke() = repository.clear()
}