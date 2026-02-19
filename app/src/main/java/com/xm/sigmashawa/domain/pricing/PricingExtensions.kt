package com.xm.sigmashawa.domain.pricing

import com.xm.sigmashawa.domain.model.*

fun CartItem.price(): Money {
    val base = product.basePrice()
    val extrasSum = extras.sumOf { it.extraPrice } * count

    val multiplier = when (product) {
        is Product.Shawarma -> product.size.priceMultiplier
        is Product.Drink -> 1.0
    }

    val baseWithMultiplier = (base.amount * multiplier).toInt() * count
    return Money(baseWithMultiplier + extrasSum, base.currency)
}

fun List<CartItem>.total(): Money =
    this.fold(Money(0)) { acc, item -> acc + item.price() }
