package com.example.a10_1d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ShareProfileActivity : ComponentActivity() {
    private lateinit var previewText: TextView
    private var currentShareText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_profile)

        previewText = findViewById(R.id.shareProfilePreviewText)

        findViewById<Button>(R.id.shareProfileSendButton).setOnClickListener {
            sharePublicProfile()
        }

        findViewById<Button>(R.id.shareProfileCancelButton).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadPublicProfilePreview()
    }

    private fun loadPublicProfilePreview() {
        lifecycleScope.launch {
            val attempts = HistoryRepository(applicationContext).getAllAttempts()
            val summary = ShareProfileBuilder.buildSummary(AppData.studentProfile, attempts)
            currentShareText = ShareProfileBuilder.buildShareText(summary)
            previewText.text = currentShareText
        }
    }

    private fun sharePublicProfile() {
        if (currentShareText.isBlank()) {
            return
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Public Learning Profile")
            putExtra(Intent.EXTRA_TEXT, currentShareText)
        }

        startActivity(Intent.createChooser(sendIntent, "Share public profile"))
    }
}
