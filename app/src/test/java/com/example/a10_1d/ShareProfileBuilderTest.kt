package com.example.a10_1d

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareProfileBuilderTest {
    @Test
    fun buildShareText_includesPublicProgressOnly() {
        val profile = StudentProfile(
            name = "Taylor Smith",
            email = "taylor@example.com",
            phone = "0400 000 000",
            selectedInterests = mutableListOf("Algorithms", "Testing"),
            learningHistory = emptyList()
        )
        val attempts = listOf(
            attempt(isCorrect = true),
            attempt(isCorrect = true),
            attempt(isCorrect = false)
        )

        val summary = ShareProfileBuilder.buildSummary(profile, attempts)
        val shareText = ShareProfileBuilder.buildShareText(summary)

        assertEquals("Taylor Smith", summary.displayName)
        assertEquals(3, summary.totalAttempts)
        assertEquals(2, summary.correctAnswers)
        assertEquals(1, summary.incorrectAnswers)
        assertEquals(67, summary.accuracyPercentage)
        assertTrue(shareText.contains("Selected topics: Algorithms, Testing"))
        assertTrue(shareText.contains("Account level: Explorer"))
        assertFalse(shareText.contains(profile.email))
        assertFalse(shareText.contains(profile.phone))
        assertFalse(shareText.contains("password", ignoreCase = true))
        assertFalse(shareText.contains("api key", ignoreCase = true))
        assertFalse(shareText.contains("payment", ignoreCase = true))
    }

    private fun attempt(isCorrect: Boolean): HistoryEntity {
        return HistoryEntity(
            topic = "Algorithms",
            generatedQuestion = "Question",
            userAnswer = "Answer",
            correctAnswer = "Correct answer",
            geminiFeedback = "Feedback",
            isCorrect = isCorrect,
            createdAt = 0L
        )
    }
}
