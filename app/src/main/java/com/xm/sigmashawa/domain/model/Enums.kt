package com.xm.sigmashawa.domain.model

enum class Size(val priceMultiplier: Double) { SMALL(1.0), MEDIUM(1.2), LARGE(1.4) }

enum class Ingredient(val extraPrice: Int) {
    CHEESE(40), JALAPENO(30), EXTRA_MEAT(90), PICKLES(20), BBQ_SAUCE(25)
}

enum class OrderStatus { CREATED, PAID, COOKING, READY, CANCELLED }
