
package com.example.levelupgamer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.LevelUpViewModel

@Composable
fun CartSheet(vm: LevelUpViewModel) {
    val ui by vm.ui.collectAsState()
    val subtotal = ui.cart.sumOf { it.product.price * it.qty }
    val discount = if (ui.user?.isDuoc == true) (subtotal * 0.2).toInt() else 0
    val total = subtotal - discount

    Card(Modifier.fillMaxWidth().padding(8.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("Carrito")
            ui.cart.forEach { ci ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${ci.qty}× ${ci.product.name}")
                    Row {
                        Text("$${"%,d".format(ci.product.price * ci.qty)}")
                        Spacer(Modifier.width(8.dp))
                        OutlinedButton(onClick = { vm.changeQty(ci.product.id, -1) }) { Text("-") }
                        OutlinedButton(onClick = { vm.changeQty(ci.product.id, +1) }) { Text("+") }
                    }
                }
            }
            Divider(Modifier.padding(vertical = 6.dp))
            if (discount > 0) Text("Descuento DUOC: -$${"%,d".format(discount)}")
            Text("Total: $${"%,d".format(total)}")
            Spacer(Modifier.height(8.dp))
            Button(onClick = { vm.pay() }, enabled = ui.cart.isNotEmpty() && ui.user != null) {
                Text("Pagar")
            }
            if (ui.user == null) Text("Inicia sesión para pagar", color = MaterialTheme.colorScheme.error)
        }
    }
}
