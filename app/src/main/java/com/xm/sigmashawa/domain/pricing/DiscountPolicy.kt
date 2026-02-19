package com.xm.sigmashawa.domain.pricing

import com.xm.sigmashawa.domain.model.Money

interface DiscountPolicy {
    fun apply(total: Money): Money
}

object NoDiscount : DiscountPolicy {
    override fun apply(total: Money): Money = total
}

class PercentDiscount(private val percent: Int) : DiscountPolicy {
    init { require(percent in 0..100) }

    override fun apply(total: Money): Money {
        val discounted = total.amount - (total.amount * percent / 100)
        return total.copy(amount = discounted)
    }
}
