
package com.example.levelupgamer.data

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val category: String,
    val image: String
)

data class CartItem(val product: Product, val qty: Int = 1)

data class User(
    val email: String,
    val name: String,
    val isDuoc: Boolean,
    val points: Int = 0,
    val prefs: Set<String> = emptySet(),
    val code: String = "",
    val purchases: List<Pair<String, Int>> = emptyList(),
    val photoUri: String? = null
)

data class Review(
    val productId: String,
    val authorEmail: String,
    val stars: Int,
    val text: String,
    val ts: Long = System.currentTimeMillis()
)
