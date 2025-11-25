package com.example.levelupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelupgamer.viewmodel.LevelUpViewModel
import com.example.levelupgamer.ui.theme.*

@Composable
fun LoginScreen(
    vm: LevelUpViewModel,
    onBack: () -> Unit,
    onSuccessLogin: (String, Boolean) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    var isRegister by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (isRegister) "Registro" else "Inicio de Sesión",
            fontSize = 26.sp,
            color = BlueNeon,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // ---------- REGISTRO ----------
        if (isRegister) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = GreenNeon,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = GreenNeon
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it.filter { c -> c.isDigit() } },
                label = { Text("Edad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = GreenNeon,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = GreenNeon
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // ---------- CAMPOS COMUNES ----------
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = BlueNeon,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = BlueNeon
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = RedGamer,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = RedGamer
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- BOTÓN PRINCIPAL ----------
        Button(
            onClick = {
                message = ""
                success = false

                if (isRegister) {
                    // ------- LÓGICA DE REGISTRO -------
                    val ageInt = age.toIntOrNull() ?: 0
                    when {
                        name.isBlank() || email.isBlank() || password.isBlank() || age.isBlank() ->
                            message = "⚠Debes llenar todos los campos."
                        name.length < 4 ->
                            message = "El nombre debe tener más de 3 caracteres."
                        !email.contains("@") ->
                            message = "Ingresa un correo válido."
                        password.length < 4 ->
                            message = "La contraseña debe tener al menos 4 caracteres."
                        ageInt < 18 ->
                            message = "Debes ser mayor de 18 años para registrarte."
                        else -> {
                            vm.registerUser(
                                name = name,
                                email = email
                            ) { ok, msg ->
                                success = ok
                                message = msg
                                if (ok) {
                                    // volvemos a login
                                    isRegister = false
                                    password = ""
                                }
                            }
                        }
                    }
                } else {
                    // ------- LÓGICA DE LOGIN (SIN LISTA LOCAL) -------
                    when {
                        email.isBlank() || password.isBlank() ->
                            message = "⚠️ Completa todos los campos."
                        !email.contains("@") ->
                            message = "❌ Ingresa un correo válido."
                        else -> {
                            val duoc = email.endsWith("@duocuc.cl", ignoreCase = true)
                            val loginName = email.substringBefore("@")

                            // guardamos usuario en prefs (simula login)
                            vm.signIn(email, loginName)

                            success = true
                            message = if (duoc)
                                "Bienvenido $loginName! Tienes 20% de descuento 🎉"
                            else
                                "Bienvenido $loginName!"

                            // avisamos al MainActivity
                            onSuccessLogin(loginName, duoc)
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
        ) {
            Text(
                if (isRegister) "Registrar" else "Ingresar",
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            message,
            color = if (success) GreenNeon else Color.Red,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextButton(onClick = { isRegister = !isRegister }) {
            Text(
                if (isRegister)
                    "¿Ya tienes cuenta? Inicia sesión aquí"
                else
                    "¿No tienes cuenta? Regístrate aquí",
                color = BlueNeon
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = { onBack() }) {
            Text("⬅ Volver", color = RedGamer)
        }
    }
}
