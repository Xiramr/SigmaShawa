package com.xm.sigmashawa.domain.service

import com.xm.sigmashawa.domain.model.Money

abstract class PaymentMethod(val displayName: String) {
    abstract fun pay(amount: Money): PaymentResult
}

sealed interface PaymentResult {
    data class Success(val transactionId: String) : PaymentResult
    data class Failure(val reason: String) : PaymentResult
}

class CashPayment : PaymentMethod("Наличные") {
    override fun pay(amount: Money): PaymentResult =
        PaymentResult.Success("CASH-${System.currentTimeMillis()}")
}

class CardPayment : PaymentMethod("Карта") {
    override fun pay(amount: Money): PaymentResult =
        PaymentResult.Success("CARD-${System.currentTimeMillis()}")
}
