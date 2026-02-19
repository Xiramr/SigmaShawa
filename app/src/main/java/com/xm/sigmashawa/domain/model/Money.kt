package com.xm.sigmashawa.domain.model

data class Money(val amount: Int, val currency: String = "RUB") {
    init { require(amount >= 0) }
    operator fun plus(other: Money): Money {
        require(currency == other.currency)
        return copy(amount = amount + other.amount)
    }
}
