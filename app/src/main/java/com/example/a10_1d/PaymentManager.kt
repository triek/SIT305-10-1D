package com.example.a10_1d

object PaymentManager {
    fun processMockGooglePayPayment(plan: AccountPlan): PaymentResult {
        return PaymentResult(
            isSuccessful = plan.level != AccountLevel.BASIC,
            message = if (plan.level == AccountLevel.BASIC) {
                "No Google Pay payment is required for the Basic plan"
            } else {
                "Mock Google Pay payment successful"
            },
            transactionReference = "MOCK-GPAY-${plan.level.name}-${System.currentTimeMillis()}"
        )
    }
}
