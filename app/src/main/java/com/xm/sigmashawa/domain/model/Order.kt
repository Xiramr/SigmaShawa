package com.xm.sigmashawa.domain.model

class Order private constructor(
    val id: String,
    val items: List<CartItem>,
    val status: OrderStatus
) {
    class Builder(private val id: String) {
        private val items = mutableListOf<CartItem>()

        fun add(item: CartItem): Builder {
            items.add(item)
            return this
        }

        fun build(): Order = Order(
            id = id,
            items = items.toList(),
            status = OrderStatus.CREATED
        )
    }
}
