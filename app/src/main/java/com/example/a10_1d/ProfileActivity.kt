package com.example.a10_1d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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
            startActivity(Intent(this, ShareProfileActivity::class.java))
        }

        findViewById<Button>(R.id.profileUpgradeButton).setOnClickListener {
            startActivity(Intent(this, UpgradeAccountActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        AppData.loadAccountLevel(applicationContext)
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
            val summary = ShareProfileBuilder.buildSummary(AppData.studentProfile, attempts)

            totalAttemptsText.text = summary.totalAttempts.toString()
            correctAnswersText.text = summary.correctAnswers.toString()
            incorrectAnswersText.text = summary.incorrectAnswers.toString()
            accuracyText.text = "${summary.accuracyPercentage}%"
            accountLevelText.text = AppData.currentAccountPlan.name
            progressSummaryText.text = buildProgressSummary(summary)
        }
    }

    private fun buildProgressSummary(summary: PublicProfileSummary): String {
        return "Plan: ${AppData.currentAccountPlan.name}\n" +
            "Attempts: ${summary.totalAttempts}\n" +
            "Correct: ${summary.correctAnswers}\n" +
            "Needs review: ${summary.incorrectAnswers}\n" +
            "Accuracy: ${summary.accuracyPercentage}%"
    }

}
