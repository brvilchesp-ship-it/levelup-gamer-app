package com.example.levelupgamer.viewmodel

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.*
import com.example.levelupgamer.remote.UserDto
import com.example.levelupgamer.repository.UserRepository
import com.example.levelupgamer.repository.ProductRepository
import com.example.levelupgamer.repository.CartRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.max

data class UiState(
    val user: User? = null,
    val products: List<Product> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val message: String? = null
)

class LevelUpViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = Prefs(app)
    private val userRepo = UserRepository()
    private val productRepo = ProductRepository()
    private val cartRepo = CartRepository()

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui.asStateFlow()

    init {
        // Usuario guardado en SharedPreferences
        viewModelScope.launch {
            prefs.userFlow.collect { u ->
                _ui.update { it.copy(user = u) }
            }
        }

        // Cargar productos desde backend
        loadProducts()
    }

    // ================= USUARIOS =================
    fun registerUser(
        name: String,
        email: String,
        onResult: (Boolean, String) -> Unit
    ) = viewModelScope.launch {
        try {
            val isDuoc = email.endsWith("@duocuc.cl", ignoreCase = true)

            val dto = UserDto(
                email = email,
                name = name,
                duoc = isDuoc,
                points = 0
            )

            val saved = userRepo.registerUser(dto)

            val localUser = User(
                email = saved.email,
                name = saved.name,
                isDuoc = saved.duoc,
                points = saved.points
            )

            prefs.saveUser(localUser)
            onResult(true, "Usuario registrado correctamente 🎮")

        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false, "Error al registrar: ${e.message}")
        }
    }

    fun signIn(email: String, name: String) = viewModelScope.launch {
        val isDuoc = email.endsWith("@duocuc.cl", ignoreCase = true)
        val user = User(email = email, name = name, isDuoc = isDuoc)
        prefs.saveUser(user)
    }

    fun signOut() = viewModelScope.launch { prefs.signOut() }

    // ================= PRODUCTOS =================
    fun loadProducts() = viewModelScope.launch {
        try {
            val list = productRepo.getProducts()
            _ui.update { it.copy(products = list, message = null) }
        } catch (e: Exception) {
            e.printStackTrace()
            _ui.update {
                it.copy(
                    products = emptyList(),
                    message = "No se pudo cargar productos desde el servidor."
                )
            }
        }
    }

    fun seedProducts() = viewModelScope.launch {
        try {
            for (p in sampleProducts()) {
                productRepo.createProduct(p)
            }
            loadProducts()
            _ui.update { it.copy(message = "Productos demo creados en el servidor") }
        } catch (e: Exception) {
            e.printStackTrace()
            _ui.update { it.copy(message = "Error al crear productos demo: ${e.message}") }
        }
    }

    // ================= CARRITO =================
    fun addToCart(p: Product) = viewModelScope.launch {
        vibrate()
        val list = _ui.value.cart.toMutableList()
        val idx = list.indexOfFirst { it.product.id == p.id }
        if (idx >= 0) {
            list[idx] = list[idx].copy(qty = list[idx].qty + 1)
        } else {
            list.add(CartItem(p, 1))
        }
        _ui.update { it.copy(cart = list) }
    }

    fun removeFromCart(id: String) {
        _ui.update {
            it.copy(cart = it.cart.filterNot { ci -> ci.product.id == id })
        }
    }

    fun changeQty(id: String, delta: Int) {
        _ui.update {
            it.copy(
                cart = it.cart.map { ci ->
                    if (ci.product.id == id)
                        ci.copy(qty = max(1, ci.qty + delta))
                    else ci
                }
            )
        }
    }

    // 🔥 Pago conectado al backend /api/cart/checkout
    fun pay() = viewModelScope.launch {
        val currentCart = _ui.value.cart
        val currentUser = _ui.value.user

        if (currentCart.isEmpty()) {
            _ui.update { it.copy(message = "Tu carrito está vacío") }
            return@launch
        }

        if (currentUser == null) {
            _ui.update { it.copy(message = "Debes iniciar sesión para pagar") }
            return@launch
        }

        try {
            val resp = cartRepo.checkout(currentCart)

            val updatedUser = currentUser.copy(points = resp.newPointsBalance)
            prefs.saveUser(updatedUser)

            _ui.update {
                it.copy(
                    user = updatedUser,
                    cart = emptyList(),
                    message = "Pago exitoso 🎉 Total $${"%,d".format(resp.total)} (+${resp.earnedPoints} pts)"
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            _ui.update {
                it.copy(message = "Error al procesar el pago: ${e.message}")
            }
        }
    }

    // ================= UTILIDADES =================
    private fun vibrate() {
        getApplication<Application>().getSystemService<Vibrator>()?.let { vib ->
            vib.vibrate(
                VibrationEffect.createOneShot(
                    30,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }
}

// Productos demo
fun sampleProducts() = listOf(
    Product(
        "ps5",
        "PlayStation 5",
        599_990,
        "Consolas",
        "https://media.falabella.com/falabellaCL/144879483_01/w=1500,h=1500,fit=pad"
    ),
    Product(
        "pc-rog",
        "PC Gamer ASUS ROG Strix",
        980_000,
        "Computadores Gamers",
        "https://media.solotodo.com/media/products/1376804_picture_1619193737.jpg"
    ),
    Product(
        "hyperx",
        "Auriculares Gamer HyperX Cloud II",
        36_990,
        "Accesorios",
        "https://media.solotodo.com/media/products/1666477_picture_1668178725.jpg"
    ),
    Product(
        "pc-pba",
        "PC Gaming Asus Rog Strix PBA",
        5_699_990,
        "Computadores Gamers",
        "https://www.xtremepc.com.mx/cdn/shop/files/f2d07544-3b3d-49d1-bd86-f2ec23b62c8e_800x.png?v=1732267809"
    ),
    Product(
        "apexpro",
        "Apex Pro TKL Gen3",
        300_790,
        "Accesorios",
        "https://http2.mlstatic.com/D_NQ_NP_777006-MLA80570414748_112024-O.webp"
    ),
    Product(
        "polera",
        "Polera Gamer Personalizada 'Level-Up'",
        14_990,
        "Poleras Personalizadas",
        "https://cdnx.jumpseller.com/estampados-bettoskys/image/29748856/resize/640/640?1669413482"
    )
)
