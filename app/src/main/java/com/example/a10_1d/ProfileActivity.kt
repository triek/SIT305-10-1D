package com.example.a10_1d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileActivity : ComponentActivity() {
    private lateinit var totalAttemptsText: TextView
    private lateinit var correctAnswersText: TextView
    private lateinit var incorrectAnswersText: TextView
    private lateinit var accuracyText: TextView
    private lateinit var accountLevelText: TextView
    private lateinit var progressSummaryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        totalAttemptsText = findViewById(R.id.totalAttemptsText)
        correctAnswersText = findViewById(R.id.correctAnswersText)
        incorrectAnswersText = findViewById(R.id.incorrectAnswersText)
        accuracyText = findViewById(R.id.accuracyText)
        accountLevelText = findViewById(R.id.accountLevelText)
        progressSummaryText = findViewById(R.id.progressSummaryText)

        findViewById<Button>(R.id.profileHistoryButton).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<Button>(R.id.profileShareButton).setOnClickListener {
            shareProfile()
        }

        findViewById<Button>(R.id.profileUpgradeButton).setOnClickListener {
            startActivity(Intent(this, UpgradeAccountActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        showAccountInformation()
        loadLearningStatistics()
    }

    private fun showAccountInformation() {
        val profile = AppData.studentProfile
        findViewById<TextView>(R.id.profileNameText).text = profile.name
        findViewById<TextView>(R.id.profileEmailText).text = profile.email
        findViewById<TextView>(R.id.profilePhoneText).text = profile.phone
        findViewById<TextView>(R.id.profileInterestsText).text =
            if (profile.selectedInterests.isEmpty()) {
                "No learning interests selected yet"
            } else {
                profile.selectedInterests.joinToString(separator = " • ")
            }
    }

    private fun loadLearningStatistics() {
        lifecycleScope.launch {
            val attempts = HistoryRepository(applicationContext).getAllAttempts()
            val totalAttempts = attempts.size
            val correctAnswers = attempts.count { it.isCorrect }
            val incorrectAnswers = totalAttempts - correctAnswers
            val accuracy = if (totalAttempts == 0) {
                0
            } else {
                ((correctAnswers.toDouble() / totalAttempts) * 100).roundToInt()
            }
            val accountLevel = calculateAccountLevel(totalAttempts, accuracy)

            totalAttemptsText.text = totalAttempts.toString()
            correctAnswersText.text = correctAnswers.toString()
            incorrectAnswersText.text = incorrectAnswers.toString()
            accuracyText.text = "$accuracy%"
            accountLevelText.text = accountLevel
            progressSummaryText.text = buildProgressSummary(totalAttempts, correctAnswers, incorrectAnswers, accuracy, accountLevel)
        }
    }

    private fun calculateAccountLevel(totalAttempts: Int, accuracy: Int): String {
        return when {
            totalAttempts == 0 -> "Starter"
            totalAttempts >= 12 && accuracy >= 85 -> "Advanced"
            totalAttempts >= 8 && accuracy >= 70 -> "Proficient"
            totalAttempts >= 4 -> "Developing"
            else -> "Explorer"
        }
    }

    private fun buildProgressSummary(
        totalAttempts: Int,
        correctAnswers: Int,
        incorrectAnswers: Int,
        accuracy: Int,
        accountLevel: String
    ): String {
        return "Level: $accountLevel\n" +
            "Attempts: $totalAttempts\n" +
            "Correct: $correctAnswers\n" +
            "Needs review: $incorrectAnswers\n" +
            "Accuracy: $accuracy%"
    }

    private fun shareProfile() {
        val shareText = buildString {
            appendLine("${AppData.studentProfile.name}'s Learning Profile")
            appendLine(progressSummaryText.text)
            appendLine("Interests: ${AppData.studentProfile.selectedInterests.joinToString()}")
        }
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Learning Profile")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(sendIntent, "Share Profile"))
    }
}
