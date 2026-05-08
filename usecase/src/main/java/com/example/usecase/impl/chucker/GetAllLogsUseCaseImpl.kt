package com.example.usecase.impl.chucker

import com.example.entity.repository.intercepter.InterceptorRepository
import com.example.usecase.GetNetworkLogsUseCase
import com.example.usecase.mapper.toDomain
import com.example.usecase.model.NetworkLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllLogsUseCaseImpl @Inject constructor(
    private val repository: InterceptorRepository
) : GetNetworkLogsUseCase {
    override fun invoke(): Flow<List<NetworkLog>> =
        repository.getAllLogs().map { list -> list.map { it.toDomain() } }
}