package com.example.a10_1d

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

class HistoryActivity : ComponentActivity() {
    private lateinit var historyListContainer: LinearLayout
    private lateinit var emptyHistoryText: TextView
    private val dateFormatter: DateFormat by lazy {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    private fun createContentView(): View {
        val scrollView = ScrollView(this).apply {
            setBackgroundResource(R.drawable.bg_gradient)
            isFillViewport = true
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28.dp(), 48.dp(), 28.dp(), 28.dp())
        }

        root.addView(TextView(this).apply {
            text = "Learning History"
            setTextColor(getColor(R.color.text_black))
            textSize = 34f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })

        root.addView(TextView(this).apply {
            text = "Review your completed quiz attempts and Gemini feedback. Premium unlocks extended history access."
            setTextColor(getColor(R.color.text_black))
            textSize = 16f
            setPadding(0, 8.dp(), 0, 20.dp())
        })

        emptyHistoryText = TextView(this).apply {
            text = "No completed quiz attempts yet. Submit a generated task to build your history."
            setTextColor(getColor(R.color.text_black))
            textSize = 16f
            visibility = View.GONE
        }
        root.addView(emptyHistoryText)

        historyListContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        root.addView(historyListContainer)

        scrollView.addView(root)
        return scrollView
    }

    private fun loadHistory() {
        AppData.loadAccountLevel(applicationContext)
        lifecycleScope.launch {
            val attempts = HistoryRepository(applicationContext).getAllAttempts()
            val visibleAttempts = if (AppData.hasPremiumAccess()) attempts else attempts.take(BASIC_HISTORY_LIMIT)
            historyListContainer.removeAllViews()
            emptyHistoryText.visibility = if (attempts.isEmpty()) View.VISIBLE else View.GONE

            if (attempts.isNotEmpty()) {
                historyListContainer.addView(createPlanNoticeView(attempts.size, visibleAttempts.size))
            }

            visibleAttempts.forEach { attempt ->
                historyListContainer.addView(createHistoryItemView(attempt))
            }
        }
    }

    private fun createPlanNoticeView(totalAttempts: Int, visibleAttempts: Int): View {
        val notice = TextView(this).apply {
            val planName = AppData.currentAccountPlan.name
            text = if (AppData.hasPremiumAccess()) {
                "$planName plan: extended history enabled. Showing all $totalAttempts attempts."
            } else {
                "$planName plan: showing latest $visibleAttempts of $totalAttempts attempts. Upgrade to Premium for extended history access."
            }
            setTextColor(getColor(R.color.text_black))
            textSize = 15f
            setPadding(0, 0, 0, 14.dp())
        }
        return notice
    }

    private fun createHistoryItemView(attempt: HistoryEntity): View {
        val item = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_blue_card)
            setPadding(18.dp(), 16.dp(), 18.dp(), 16.dp())
            isClickable = true
            isFocusable = true
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 14.dp()
        }
        item.layoutParams = layoutParams

        item.addView(TextView(this).apply {
            text = attempt.topic
            setTextColor(getColor(R.color.card_text_white))
            textSize = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })

        item.addView(TextView(this).apply {
            text = if (attempt.isCorrect) "Result: Correct" else "Result: Needs review"
            setTextColor(getColor(R.color.card_text_white))
            textSize = 15f
            setPadding(0, 6.dp(), 0, 0)
        })

        item.addView(TextView(this).apply {
            text = "Completed: ${dateFormatter.format(Date(attempt.createdAt))}"
            setTextColor(getColor(R.color.card_text_white))
            textSize = 14f
            setPadding(0, 4.dp(), 0, 0)
        })

        item.setOnClickListener { showHistoryDetails(attempt) }
        return item
    }

    private fun showHistoryDetails(attempt: HistoryEntity) {
        val detailView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24.dp(), 12.dp(), 24.dp(), 0)
        }

        detailView.addDetailText("Question", attempt.generatedQuestion)
        detailView.addDetailText("Submitted answer", attempt.userAnswer)
        detailView.addDetailText("Correct answer", attempt.correctAnswer)
        detailView.addDetailText("Gemini feedback", attempt.geminiFeedback)

        AlertDialog.Builder(this)
            .setTitle(attempt.topic)
            .setView(detailView)
            .setPositiveButton("Close", null)
            .show()
    }

    private fun LinearLayout.addDetailText(label: String, body: String) {
        addView(TextView(this@HistoryActivity).apply {
            text = label
            setTextColor(getColor(R.color.text_black))
            textSize = 16f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 12.dp(), 0, 4.dp())
        })
        addView(TextView(this@HistoryActivity).apply {
            text = body
            setTextColor(getColor(R.color.text_black))
            textSize = 15f
        })
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()

    private companion object {
        const val BASIC_HISTORY_LIMIT = 3
    }
}
