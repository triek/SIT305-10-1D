package com.example.a10_1d

data class PaymentResult(
    val isSuccessful: Boolean,
    val message: String,
    val transactionReference: String
)
