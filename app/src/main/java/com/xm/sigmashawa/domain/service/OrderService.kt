package com.xm.sigmashawa.domain.service

import com.xm.sigmashawa.domain.model.*
import com.xm.sigmashawa.domain.pricing.DiscountPolicy
import com.xm.sigmashawa.domain.pricing.NoDiscount
import com.xm.sigmashawa.domain.pricing.total
import com.xm.sigmashawa.domain.repository.MenuRepository

class OrderService(
    private val menuRepository: MenuRepository,
    private val discountPolicy: DiscountPolicy = NoDiscount
) {
    fun createOrder(orderId: String, items: List<CartItem>): Order =
        Order.Builder(orderId).apply { items.forEach { add(it) } }.build()

    fun calculateTotal(items: List<CartItem>): Money {
        val total = items.total()
        return discountPolicy.apply(total)
    }

    fun <T : Product> onlyType(menu: List<Product>, clazz: Class<T>): List<T> =
        menu.mapNotNull { p -> if (clazz.isInstance(p)) clazz.cast(p) else null }

    fun validateItem(item: CartItem): String? {
        val shawarma = item.product as? Product.Shawarma
        if (shawarma != null && shawarma.spicyLevel !in 0..3) return "Spicy level must be 0..3"
        if (Ingredient.EXTRA_MEAT in item.extras && item.count > 3) return "Too many EXTRA_MEAT items at once"
        return null
    }

    fun addToCart(productId: String, extras: List<Ingredient>, count: Int): CartItem {
        val product = menuRepository.findById(productId)
            ?: throw IllegalArgumentException("Product not found: $productId")
        return CartItem(product = product, extras = extras, count = count)
    }
}
