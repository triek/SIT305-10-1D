package com.example.a10_1d

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.app.AlertDialog
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class UpgradeAccountActivity : ComponentActivity() {
    private lateinit var currentPlanText: TextView
    private lateinit var plansContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade_account)

        AppData.loadAccountLevel(applicationContext)
        currentPlanText = findViewById(R.id.currentPlanText)
        plansContainer = findViewById(R.id.accountPlansContainer)

        findViewById<TextView>(R.id.upgradeNameText).text = AppData.studentProfile.name
        findViewById<Button>(R.id.manageInterestsButton).setOnClickListener {
            startActivity(Intent(this, InterestSelectionActivity::class.java))
        }
        findViewById<Button>(R.id.backToProfileButton).setOnClickListener {
            finish()
        }

        renderAccountPlans()
    }

    private fun renderAccountPlans() {
        currentPlanText.text = "Current plan: ${AppData.currentAccountPlan.name}"
        plansContainer.removeAllViews()
        AccountPlans.all.forEach { plan ->
            plansContainer.addView(createPlanCard(plan))
        }
    }

    private fun createPlanCard(plan: AccountPlan): LinearLayout {
        val isCurrentPlan = plan.level == AppData.currentAccountLevel
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_blue_card)
            setPadding(20.dp(), 18.dp(), 20.dp(), 18.dp())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 16.dp() }

            addView(TextView(this@UpgradeAccountActivity).apply {
                text = if (isCurrentPlan) "${plan.name} • Current plan" else plan.name
                setTextColor(getColor(R.color.card_text_white))
                textSize = 24f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            })

            addView(TextView(this@UpgradeAccountActivity).apply {
                text = plan.price
                setTextColor(getColor(R.color.card_text_white))
                textSize = 18f
                setPadding(0, 6.dp(), 0, 10.dp())
            })

            addSection("Included features", plan.includedFeatures)
            addSection("Feature limits", plan.featureLimits)

            addView(Button(this@UpgradeAccountActivity).apply {
                text = if (isCurrentPlan) "Selected" else "Choose ${plan.name}"
                isEnabled = !isCurrentPlan
                isAllCaps = false
                setTextColor(getColor(R.color.text_black))
                setBackgroundResource(R.drawable.bg_green_button)
                gravity = Gravity.CENTER
                setOnClickListener {
                    if (plan.level == AccountLevel.BASIC) {
                        AppData.saveAccountLevel(applicationContext, plan.level)
                        Toast.makeText(
                            this@UpgradeAccountActivity,
                            "${plan.name} plan selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        renderAccountPlans()
                    } else {
                        showPaymentConfirmation(plan)
                    }
                }
            })
        }
    }

    private fun showPaymentConfirmation(plan: AccountPlan) {
        val planSummary = buildString {
            appendLine("Selected plan: ${plan.name}")
            appendLine("Price: ${plan.price}")
            appendLine()
            appendLine("Features:")
            plan.includedFeatures.forEach { feature -> appendLine("• $feature") }
        }

        AlertDialog.Builder(this)
            .setTitle("Mock Google Pay")
            .setMessage(planSummary)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Confirm payment") { _, _ ->
                handleMockPayment(plan)
            }
            .show()
    }

    private fun handleMockPayment(plan: AccountPlan) {
        val paymentResult = PaymentManager.processMockGooglePayPayment(plan)
        Toast.makeText(this, paymentResult.message, Toast.LENGTH_SHORT).show()

        if (paymentResult.isSuccessful) {
            AppData.accountLevel = plan.name
            AppData.saveAccountLevel(applicationContext, AppData.accountLevel)
            renderAccountPlans()
        }
    }

    private fun LinearLayout.addSection(title: String, items: List<String>) {
        addView(TextView(this@UpgradeAccountActivity).apply {
            text = title
            setTextColor(getColor(R.color.card_text_white))
            textSize = 16f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 10.dp(), 0, 4.dp())
        })
        items.forEach { item ->
            addView(TextView(this@UpgradeAccountActivity).apply {
                text = "• $item"
                setTextColor(getColor(R.color.card_text_white))
                textSize = 15f
                setPadding(0, 2.dp(), 0, 0)
            })
        }
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}
