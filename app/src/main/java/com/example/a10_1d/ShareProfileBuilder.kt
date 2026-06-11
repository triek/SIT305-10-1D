package com.example.a10_1d

import kotlin.math.roundToInt

data class PublicProfileSummary(
    val displayName: String,
    val selectedTopics: List<String>,
    val totalAttempts: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val accuracyPercentage: Int,
    val accountLevel: String
)

object ShareProfileBuilder {
    fun buildSummary(profile: StudentProfile, attempts: List<HistoryEntity>): PublicProfileSummary {
        val totalAttempts = attempts.size
        val correctAnswers = attempts.count { it.isCorrect }
        val incorrectAnswers = totalAttempts - correctAnswers
        val accuracyPercentage = calculateAccuracyPercentage(correctAnswers, totalAttempts)

        return PublicProfileSummary(
            displayName = profile.name,
            selectedTopics = profile.selectedInterests.toList(),
            totalAttempts = totalAttempts,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            accuracyPercentage = accuracyPercentage,
            accountLevel = calculateAccountLevel(totalAttempts, accuracyPercentage)
        )
    }

    fun buildShareText(summary: PublicProfileSummary): String {
        val topics = if (summary.selectedTopics.isEmpty()) {
            "No public topics selected yet"
        } else {
            summary.selectedTopics.joinToString(separator = ", ")
        }

        return buildString {
            appendLine("${summary.displayName}'s Public Learning Profile")
            appendLine()
            appendLine("Selected topics: $topics")
            appendLine("Total attempts: ${summary.totalAttempts}")
            appendLine("Correct answers: ${summary.correctAnswers}")
            appendLine("Incorrect answers: ${summary.incorrectAnswers}")
            appendLine("Accuracy: ${summary.accuracyPercentage}%")
            appendLine("Account level: ${summary.accountLevel}")
            appendLine()
            append("Shared from SIT305-10.1D")
        }
    }

    fun calculateAccountLevel(totalAttempts: Int, accuracyPercentage: Int): String {
        return when {
            totalAttempts == 0 -> "Starter"
            totalAttempts >= 12 && accuracyPercentage >= 85 -> "Advanced"
            totalAttempts >= 8 && accuracyPercentage >= 70 -> "Proficient"
            totalAttempts >= 4 -> "Developing"
            else -> "Explorer"
        }
    }

    private fun calculateAccuracyPercentage(correctAnswers: Int, totalAttempts: Int): Int {
        return if (totalAttempts == 0) {
            0
        } else {
            ((correctAnswers.toDouble() / totalAttempts) * 100).roundToInt()
        }
    }
}
