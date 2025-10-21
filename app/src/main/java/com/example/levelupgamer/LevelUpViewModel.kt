
package com.example.levelupgamer

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.max

data class UiState(
    val user: User? = null,
    val products: List<Product> = sampleProducts(),
    val cart: List<CartItem> = emptyList(),
    val reviews: Map<String, List<Review>> = emptyMap(),
    val message: String? = null
)

class LevelUpViewModel(app: Application): AndroidViewModel(app) {
    private val prefs = Prefs(app)
    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.userFlow.collect { u -> _ui.update { it.copy(user = u) } }
        }
    }

    fun signIn(email: String, name: String) = viewModelScope.launch {
        val isDuoc = email.endsWith("@duocuc.cl", ignoreCase = true)
        val user = User(email = email, name = name, isDuoc = isDuoc)
        prefs.saveUser(user)
    }

    fun signOut() = viewModelScope.launch { prefs.signOut() }

    fun addToCart(p: Product) = viewModelScope.launch {
        vibrate()
        val list = _ui.value.cart.toMutableList()
        val idx = list.indexOfFirst { it.product.id == p.id }
        if (idx >= 0) list[idx] = list[idx].copy(qty = list[idx].qty + 1)
        else list.add(CartItem(p, 1))
        _ui.update { it.copy(cart = list) }
    }

    fun removeFromCart(id: String) {
        _ui.update { it.copy(cart = it.cart.filterNot { it.product.id == id }) }
    }

    fun changeQty(id: String, delta: Int) {
        _ui.update {
            it.copy(cart = it.cart.map { ci ->
                if (ci.product.id == id) ci.copy(qty = max(1, ci.qty + delta)) else ci
            })
        }
    }

    fun pay() = viewModelScope.launch {
        val subtotal = _ui.value.cart.sumOf { it.product.price * it.qty }
        val discount = if (_ui.value.user?.isDuoc == true) (subtotal * 0.2).toInt() else 0
        val total = subtotal - discount
        val pts = (total / 100).toInt()

        _ui.value.user?.let { u ->
            prefs.saveUser(u.copy(points = u.points + pts))
        }
        _ui.update { it.copy(cart = emptyList(), message = "Pago exitoso (+$pts pts)") }
    }

    fun postReview(productId: String, stars: Int, text: String) {
        val email = _ui.value.user?.email ?: return
        val map = _ui.value.reviews.toMutableMap()
        val list = (map[productId] ?: emptyList()).toMutableList()
        val idx = list.indexOfFirst { it.authorEmail == email }
        val r = Review(productId, email, stars, text)
        if (idx >= 0) list[idx] = r else list.add(r)
        map[productId] = list
        _ui.update { it.copy(reviews = map) }
    }

    private fun vibrate() {
        getApplication<Application>().getSystemService<Vibrator>()?.let { vib ->
            vib.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}

// Producto de ejemplo
fun sampleProducts() = listOf(
    Product("ps5","PlayStation 5",599_990,"Consolas","https://media.falabella.com/falabellaCL/144879483_01/w=1500,h=1500,fit=pad"),
    Product("pc-rog","PC Gamer ASUS ROG Strix",980_000,"Computadores Gamers","https://media.solotodo.com/media/products/1376804_picture_1619193737.jpg"),
    Product("hyperx","Auriculares Gamer HyperX Cloud II",36_990,"Accesorios","https://media.solotodo.com/media/products/1666477_picture_1668178725.jpg"),
    Product("pc-pba","PC Gaming Asus Rog Strix PBA",5_699_990,"Computadores Gamers","https://www.xtremepc.com.mx/cdn/shop/files/f2d07544-3b3d-49d1-bd86-f2ec23b62c8e_800x.png?v=1732267809"),
    Product("apexpro","Apex Pro TKL Gen3",300_790,"Accesorios","https://http2.mlstatic.com/D_NQ_NP_777006-MLA80570414748_112024-O.webp"),
    Product("polera","Polera Gamer Personalizada 'Level-Up'",14_990,"Poleras Personalizadas","https://cdnx.jumpseller.com/estampados-bettoskys/image/29748856/resize/640/640?1669413482"),
    Product("poleron","Polerón Gamer Personalizado 'Level-Up'",42_990,"Accesorios","https://http2.mlstatic.com/D_NQ_NP_746963-MLC53433072044_012023-O.webp"),
)
