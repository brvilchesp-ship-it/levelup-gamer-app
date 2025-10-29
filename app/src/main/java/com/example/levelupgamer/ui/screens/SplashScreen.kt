package com.example.levelupgamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import com.example.levelupgamer.R

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000) // ⏳ espera 3 segundos antes de continuar
        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo LevelUpGamer",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "LevelUpGamer",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF39FF14), // verde neón
                textAlign = TextAlign.Center
            )
        }
    }
}
