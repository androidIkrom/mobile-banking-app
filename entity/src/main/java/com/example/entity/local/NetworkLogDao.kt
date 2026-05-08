package com.example.entity.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkLogDao {
    @Query("SELECT *FROM network_logs ORDER BY timeStamp DESC")
    fun getAllLogs() : Flow<List<NetworkLogEntity>>

    @Query("DELETE FROM network_logs")
    suspend fun clearLogs()
    @Insert
    suspend fun insertLog(log : NetworkLogEntity)
}