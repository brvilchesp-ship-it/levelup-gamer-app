
package com.example.levelupgamer.ui.screens

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.levelupgamer.LevelUpViewModel
import com.example.levelupgamer.domain.Validators

@Composable
fun LoginScreen(
    vm: LevelUpViewModel,
    onBack: () -> Unit,
    onPickPhoto: ((Uri?) -> Unit) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var age by remember { mutableStateOf(TextFieldValue()) }
    var photo by remember { mutableStateOf<Uri?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear cuenta / Ingresar", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Edad (18+)") })

        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            onPickPhoto { uri -> photo = uri }
        }) { Text("Elegir foto de perfil") }
        photo?.let { AsyncImage(model = it, contentDescription = null, modifier = Modifier.size(72.dp)) }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val a = age.text.toIntOrNull()
            when {
                !Validators.nameOk(name.text) -> error = "Nombre muy corto"
                !Validators.emailOk(email.text) -> error = "Correo inválido"
                !Validators.ageOk(a) -> error = "Debes ser mayor de 18"
                else -> {
                    vm.signIn(email.text.trim(), name.text.trim())
                    onBack()
                }
            }
        }) { Text("Continuar") }

        TextButton(onClick = onBack) { Text("Volver") }
    }
}
