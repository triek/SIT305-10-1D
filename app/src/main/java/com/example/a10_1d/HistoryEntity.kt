package com.example.a10_1d

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_attempts")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val generatedQuestion: String,
    val userAnswer: String,
    val correctAnswer: String,
    val geminiFeedback: String,
    val isCorrect: Boolean,
    val createdAt: Long
)
