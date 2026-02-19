package com.xm.sigmashawa.domain.model

sealed class Product(
    open val id: String,
    open val title: String
) {
    abstract fun basePrice(): Money

    data class Shawarma(
        override val id: String,
        override val title: String,
        val size: Size,
        val spicyLevel: Int,
        val base: Money
    ) : Product(id, title) {
        override fun basePrice(): Money = base
    }

    data class Drink(
        override val id: String,
        override val title: String,
        val volumeMl: Int,
        val base: Money
    ) : Product(id, title) {
        override fun basePrice(): Money = base
    }
}
