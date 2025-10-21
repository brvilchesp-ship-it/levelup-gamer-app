
package com.example.levelupgamer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.levelupgamer.LevelUpViewModel
import com.example.levelupgamer.data.Product
import com.example.levelupgamer.ui.components.CartSheet

@Composable
fun CatalogScreen(vm: LevelUpViewModel, onGoPoints: () -> Unit, onGoLogin: () -> Unit) {
    val ui by vm.ui.collectAsState()
    var showCart by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("LEVEL-UP GAMER", style = MaterialTheme.typography.headlineSmall)
            Row {
                Button(onClick = onGoPoints) { Text("Puntos") }
                Spacer(Modifier.width(8.dp))
                if (ui.user == null) Button(onClick = onGoLogin) { Text("Ingresar") }
                else Text("Hola, ${ui.user!!.name}")
                Spacer(Modifier.width(8.dp))
                Button(onClick = { showCart = !showCart }) { Text("\uD83D\uDED2 ${ui.cart.sumOf { it.qty }}") }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), contentPadding = PaddingValues(6.dp)) {
            items(ui.products) { p -> ProductCard(p) { vm.addToCart(p) } }
        }

        AnimatedVisibility(showCart) { CartSheet(vm) }
        ui.message?.let { Text(it) }
    }
}

@Composable
private fun ProductCard(p: Product, onAdd: () -> Unit) {
    Card(Modifier.padding(6.dp)) {
        Column(Modifier.padding(10.dp)) {
            AsyncImage(model = p.image, contentDescription = p.name, modifier = Modifier.height(120.dp).fillMaxWidth())
            Text(p.name, style = MaterialTheme.typography.titleMedium)
            Text("$${"%,d".format(p.price)}")
            Spacer(Modifier.height(6.dp))
            Button(onClick = onAdd) { Text("Agregar") }
        }
    }
}
