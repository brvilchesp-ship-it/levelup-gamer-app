package com.example.levelupgamer.repository

import com.example.levelupgamer.data.Product
import com.example.levelupgamer.remote.ProductRetrofitInstance

class ProductRepository {

    suspend fun getProducts(): List<Product> {
        return ProductRetrofitInstance.api.getProducts()
    }

    // 🔥 NUEVO: crear producto en el backend
    suspend fun createProduct(product: Product): Product {
        return ProductRetrofitInstance.api.createProduct(product)
    }
}
