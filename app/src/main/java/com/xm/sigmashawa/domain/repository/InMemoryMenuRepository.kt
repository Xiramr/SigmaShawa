package com.xm.sigmashawa.domain.repository

import com.xm.sigmashawa.domain.model.Money
import com.xm.sigmashawa.domain.model.Product
import com.xm.sigmashawa.domain.model.Size

class InMemoryMenuRepository : MenuRepository {

    private val menu: List<Product> = listOf(

        Product.Shawarma(
            id = "sh1",
            title = "Sigma Classic",
            size = Size.MEDIUM,
            prices = mapOf(
                Size.SMALL to Money(200),
                Size.MEDIUM to Money(240),
                Size.LARGE to Money(290)
            ),
            spicyLevel = 1
        ),

        Product.Shawarma(
            id = "sh2",
            title = "Sigma Hot",
            size = Size.LARGE,
            prices = mapOf(
                Size.SMALL to Money(220),
                Size.MEDIUM to Money(260),
                Size.LARGE to Money(320)
            ),
            spicyLevel = 3
        ),

        Product.Drink(
            id = "dr1",
            title = "Cola",
            volumeMl = 500,
            base = Money(120)
        )
    )

    override fun getMenu(): List<Product> = menu

    override fun findById(id: String): Product? =
        menu.firstOrNull { it.id == id }
}