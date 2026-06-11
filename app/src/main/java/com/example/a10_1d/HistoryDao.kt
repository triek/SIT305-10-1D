package com.example.a10_1d

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertAttempt(historyEntity: HistoryEntity): Long

    @Query("SELECT * FROM history_attempts ORDER BY createdAt DESC")
    suspend fun getAllAttempts(): List<HistoryEntity>

    @Query("SELECT * FROM history_attempts WHERE id = :id LIMIT 1")
    suspend fun getAttemptById(id: Long): HistoryEntity?
}
