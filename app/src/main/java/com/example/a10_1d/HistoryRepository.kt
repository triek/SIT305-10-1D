package com.example.a10_1d

import android.content.Context

class HistoryRepository(context: Context) {
    private val historyDao = AppDatabase.getDatabase(context).historyDao()

    suspend fun saveAttempt(historyEntity: HistoryEntity): Long {
        return historyDao.insertAttempt(historyEntity)
    }

    suspend fun getAllAttempts(): List<HistoryEntity> {
        return historyDao.getAllAttempts()
    }

    suspend fun getAttemptById(id: Long): HistoryEntity? {
        return historyDao.getAttemptById(id)
    }
}
