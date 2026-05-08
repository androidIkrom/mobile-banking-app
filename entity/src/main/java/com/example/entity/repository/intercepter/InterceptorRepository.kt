package com.example.entity.repository.intercepter

import com.example.entity.local.NetworkLogEntity
import kotlinx.coroutines.flow.Flow

interface InterceptorRepository {
    suspend fun insertLog(logEntity: NetworkLogEntity)
    fun getAllLogs(): Flow<List<NetworkLogEntity>>
    suspend fun clear()
}