package com.example.levelupgamer.repository

import com.example.levelupgamer.data.CartItem
import com.example.levelupgamer.remote.CartItemRequestDto
import com.example.levelupgamer.remote.CartRetrofitInstance
import com.example.levelupgamer.remote.CheckoutRequestDto
import com.example.levelupgamer.remote.CheckoutResponseDto

class CartRepository {

    private val api = CartRetrofitInstance.api

    /**
     * Llama al backend con los productos del carrito y devuelve el resumen del pago.
     */
    suspend fun checkout(cartItems: List<CartItem>): CheckoutResponseDto {
        val itemsDto = cartItems.map { ci ->
            CartItemRequestDto(
                productId = ci.product.id,
                qty = ci.qty
            )
        }

        val request = CheckoutRequestDto(items = itemsDto)
        return api.checkout(request)
    }
}
