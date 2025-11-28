package com.example.levelupgamer.remote

import retrofit2.http.Body
import retrofit2.http.POST

// Lo que manda la app al backend
data class CartItemRequestDto(
    val productId: String,
    val qty: Int
)

data class CheckoutRequestDto(
    val items: List<CartItemRequestDto>
)

// Lo que responde el backend
data class CheckoutLineDto(
    val productId: String,
    val name: String,
    val qty: Int,
    val unitPrice: Int,
    val lineTotal: Int
)

data class CheckoutResponseDto(
    val userEmail: String,
    val userName: String,
    val duoc: Boolean,
    val subtotal: Int,
    val discount: Int,
    val total: Int,
    val earnedPoints: Int,
    val newPointsBalance: Int,
    val lines: List<CheckoutLineDto>
)

interface CartApiService {

    @POST("api/cart/checkout")
    suspend fun checkout(@Body req: CheckoutRequestDto): CheckoutResponseDto
}
