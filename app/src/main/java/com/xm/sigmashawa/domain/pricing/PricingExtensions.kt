package com.xm.sigmashawa.domain.pricing

import com.xm.sigmashawa.domain.model.*

fun CartItem.price(): Money {
    val base = product.basePrice()
    val extrasSum = extras.sumOf { it.extraPrice } * count
    val totalBase = base.amount * count
    return Money(totalBase + extrasSum, base.currency)
}

fun List<CartItem>.total(): Money =
    this.fold(Money(0)) { acc, item -> acc + item.price() }