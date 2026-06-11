package com.example.a10_1d

enum class AccountLevel(val displayName: String) {
    BASIC("Basic"),
    PLUS("Plus"),
    PREMIUM("Premium");

    companion object {
        fun fromStoredValue(value: String?): AccountLevel {
            return entries.firstOrNull { it.name == value || it.displayName.equals(value, ignoreCase = true) }
                ?: BASIC
        }
    }
}

data class AccountPlan(
    val level: AccountLevel,
    val price: String,
    val includedFeatures: List<String>,
    val featureLimits: List<String>
) {
    val name: String = level.displayName
}

object AccountPlans {
    val all: List<AccountPlan> = listOf(
        AccountPlan(
            level = AccountLevel.BASIC,
            price = "Free",
            includedFeatures = listOf(
                "Normal quiz practice",
                "Standard answer feedback",
                "Profile and interest management"
            ),
            featureLimits = listOf(
                "Gemini help tools locked",
                "Latest 3 history items only",
                "Study plan generation locked"
            )
        ),
        AccountPlan(
            level = AccountLevel.PLUS,
            price = "\$4.99 / month",
            includedFeatures = listOf(
                "Everything in Basic",
                "Extra Gemini hint and flashcard questions",
                "Longer Gemini explanations for answers"
            ),
            featureLimits = listOf(
                "Latest 3 history items only",
                "Study plan generation locked"
            )
        ),
        AccountPlan(
            level = AccountLevel.PREMIUM,
            price = "\$9.99 / month",
            includedFeatures = listOf(
                "Everything in Plus",
                "Study plan generation",
                "Extended history access"
            ),
            featureLimits = listOf(
                "Unlimited saved history review",
                "All premium AI study tools enabled"
            )
        )
    )

    fun get(level: AccountLevel): AccountPlan = all.first { it.level == level }
}
