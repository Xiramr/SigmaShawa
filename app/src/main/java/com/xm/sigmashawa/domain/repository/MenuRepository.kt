package com.xm.sigmashawa.domain.repository

import com.xm.sigmashawa.domain.model.Product

interface MenuRepository {
    fun getMenu(): List<Product>
    fun findById(id: String): Product?
}