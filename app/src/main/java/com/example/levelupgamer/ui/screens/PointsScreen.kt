
package com.example.levelupgamer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupgamer.viewmodel.LevelUpViewModel

@Composable
fun PointsScreen(vm: LevelUpViewModel, onBack: () -> Unit) {
    val ui by vm.ui.collectAsState()
    val pts = ui.user?.points ?: 0
    val level = when {
        pts >= 1000 -> "Diamond"
        pts >= 500 -> "Gold"
        pts >= 200 -> "Silver"
        else -> "Bronze"
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Puntos LevelUp", style = MaterialTheme.typography.headlineSmall)
        Text("Nivel: $level")
        LinearProgressIndicator(progress = { (pts / 1000f).coerceIn(0f,1f) }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        Text("Total: $pts")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
