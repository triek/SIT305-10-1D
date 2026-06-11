package com.example.a10_1d

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class UpgradeAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade_account)

        findViewById<TextView>(R.id.upgradeNameText).text = AppData.studentProfile.name
        findViewById<Button>(R.id.manageInterestsButton).setOnClickListener {
            startActivity(Intent(this, InterestSelectionActivity::class.java))
        }
        findViewById<Button>(R.id.backToProfileButton).setOnClickListener {
            finish()
        }
    }
}
