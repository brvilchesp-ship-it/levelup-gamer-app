package com.example.levelupgamer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.levelupgamer.viewmodel.LevelUpViewModel
import com.example.levelupgamer.R
import com.example.levelupgamer.data.Product

@Composable
fun CatalogScreen(
    vm: LevelUpViewModel,
    userName: String,
    hasDiscount: Boolean,
    onGoPoints: () -> Unit,
    onGoLogin: () -> Unit,
    onGoExternalPosts: () -> Unit
) {
    val ui by vm.ui.collectAsState()
    var showCart by remember { mutableStateOf(false) }

    // Usuario desde ViewModel
    val currentUser = ui.user
    val isLoggedIn = currentUser != null

    // Nombre del usuario
    val displayName = currentUser?.name ?: userName

    // DESCUENTO SOLO SI EL USUARIO LOGUEADO ES DUOC (o hasDiscount)
    val descuentoActivo = currentUser?.isDuoc == true || hasDiscount
    val descuento = if (descuentoActivo) 0.8 else 1.0

    Box(Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            // HEADER
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Level-Up Gamer",
                        modifier = Modifier
                            .size(42.dp)
                            .padding(end = 6.dp)
                    )
                    Text(
                        "LEVEL-UP GAMER",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Ingresar / Cerrar sesión
                    Button(
                        onClick = {
                            if (isLoggedIn) {
                                vm.signOut()
                            } else {
                                onGoLogin()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .height(34.dp)
                            .padding(end = 6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Text(
                            if (isLoggedIn) "Cerrar sesión" else "Ingresar",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    // Carrito
                    Button(
                        onClick = { showCart = !showCart },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            "\uD83D\uDED2 ${ui.cart.sumOf { it.qty }}",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // BIENVENIDA
            if (isLoggedIn) {
                Text(
                    text = "Bienvenido, $displayName 👋",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (descuentoActivo) {
                    Text(
                        text = "🎓 Tienes un 20% de descuento por correo DUOC UC",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Botón puntos
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onGoPoints,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                ) {
                    Text("Puntos", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            // Mensaje de estado (solo si quieres mostrar errores generales)
            ui.message?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // CATÁLOGO: lo que venga de http://localhost:9090/api/products
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(ui.products) { p ->
                    val precioFinal = (p.price * descuento).toInt()
                    ProductCard(p, precioFinal) { vm.addToCart(p) }
                }
            }
        }

        // CARRITO
        AnimatedVisibility(showCart) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 12.dp,
                shadowElevation = 8.dp
            ) {
                CartSheetCustom(vm, descuentoActivo, onClose = { showCart = false })
            }
        }
    }
}

@Composable
private fun ProductCard(p: Product, precioFinal: Int, onAdd: () -> Unit) {
    Card(
        Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = p.image,
                contentDescription = p.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Text(
                p.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                if (precioFinal < p.price)
                    "Antes: $${"%,d".format(p.price)}  Ahora: $${"%,d".format(precioFinal)}"
                else
                    "$${"%,d".format(p.price)}",
                color = if (precioFinal < p.price)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(6.dp))
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Agregar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun CartSheetCustom(vm: LevelUpViewModel, hasDiscount: Boolean, onClose: () -> Unit) {
    val ui by vm.ui.collectAsState()
    val descuento = if (hasDiscount) 0.8 else 1.0

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🛒 Carrito de Compras",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            TextButton(onClick = onClose) {
                Text("Cerrar", color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(8.dp))

        if (ui.cart.isEmpty()) {
            Text("Tu carrito está vacío", color = MaterialTheme.colorScheme.onSurface)
        } else {
            ui.cart.forEach { item ->
                val precioFinal = (item.product.price * descuento).toInt()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "${item.product.name} x${item.qty}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (hasDiscount)
                            Text(
                                "Descuento aplicado",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall
                            )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "$${"%,d".format(precioFinal * item.qty)}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { vm.removeFromCart(item.product.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            Spacer(Modifier.height(8.dp))

            val total = (ui.cart.sumOf { it.product.price * it.qty } * descuento).toInt()
            Text(
                if (hasDiscount)
                    "Total con descuento: $${"%,d".format(total)}"
                else
                    "Total: $${"%,d".format(total)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* vm.pay() si quieres */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(36.dp)
            ) {
                Text("Pagar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
