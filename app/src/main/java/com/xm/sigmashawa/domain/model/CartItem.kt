package com.xm.sigmashawa.domain.model

data class CartItem(
    val product: Product,
    val extras: List<Ingredient> = emptyList(),
    val count: Int = 1
) {
    init { require(count > 0) }
}
