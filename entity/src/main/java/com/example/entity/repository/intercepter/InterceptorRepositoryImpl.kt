package com.example.entity.repository.intercepter

import com.example.entity.local.NetworkLogDao
import com.example.entity.local.NetworkLogEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterceptorRepositoryImpl @Inject constructor(
    private val networkDao: NetworkLogDao
) : InterceptorRepository{
    override suspend fun insertLog(logEntity: NetworkLogEntity) = networkDao.insertLog(logEntity)

    override fun getAllLogs(): Flow<List<NetworkLogEntity>> = networkDao.getAllLogs()

    override suspend fun clear() = networkDao.clearLogs()
}