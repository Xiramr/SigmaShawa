package com.xm.sigmashawa

import com.xm.sigmashawa.domain.model.Ingredient
import com.xm.sigmashawa.domain.pricing.PercentDiscount
import com.xm.sigmashawa.domain.repository.InMemoryMenuRepository
import com.xm.sigmashawa.domain.service.OrderService
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderServiceTest {

    @Test
    fun total_with_discount_example() {
        val repo = InMemoryMenuRepository()
        val service = OrderService(repo, PercentDiscount(10))

        val cart = listOf(
            service.addToCart(
                productId = "sh1",
                extras = listOf(Ingredient.CHEESE, Ingredient.BBQ_SAUCE),
                count = 1
            ),
            service.addToCart(
                productId = "dr1",
                extras = emptyList(),
                count = 2
            )
        )

        val total = service.calculateTotal(cart)

        assertEquals(513, total.amount)
        assertEquals("RUB", total.currency)
    }
}
