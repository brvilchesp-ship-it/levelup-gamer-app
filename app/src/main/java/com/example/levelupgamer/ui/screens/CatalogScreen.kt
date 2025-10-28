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
import com.example.levelupgamer.LevelUpViewModel
import com.example.levelupgamer.R
import com.example.levelupgamer.data.Product

@Composable
fun CatalogScreen(vm: LevelUpViewModel, onGoPoints: () -> Unit, onGoLogin: () -> Unit) {
    val ui by vm.ui.collectAsState()
    var showCart by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {

        // 📦 Contenido principal
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // 🔝 Header
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🎮 Logo + texto
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Level-Up Gamer",
                        modifier = Modifier
                            .size(42.dp) // 🔽 más pequeño
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

                // 🔘 Botones: Ingresar y Carrito más pequeños
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onGoLogin,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .height(34.dp)
                            .padding(end = 6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Text("Ingresar", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    Button(
                        onClick = { showCart = !showCart },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Text(
                            "\uD83D\uDED2 ${ui.cart.sumOf { it.qty }}",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // 🔸 Botón de puntos centrado
            Box(
                modifier = Modifier
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
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Puntos", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            // 🧩 Catálogo de productos
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(ui.products) { p ->
                    ProductCard(p) { vm.addToCart(p) }
                }
            }
        }

        // 🛒 Carrito flotante
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
                CartSheetCustom(vm, onClose = { showCart = false })
            }
        }
    }
}

@Composable
private fun ProductCard(p: Product, onAdd: () -> Unit) {
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
                "$${"%,d".format(p.price)}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
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
fun CartSheetCustom(vm: LevelUpViewModel, onClose: () -> Unit) {
    val ui by vm.ui.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 🔝 Título del carrito
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${item.product.name} x${item.qty}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
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

            Spacer(Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            Spacer(Modifier.height(8.dp))

            val total = ui.cart.sumOf { it.product.price * it.qty }
            Text(
                "Total: $${"%,d".format(total)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            // 🔘 Botón "Pagar" sin funcionalidad
            Button(
                onClick = { /* sin acción */ },
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
