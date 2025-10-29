package com.example.levelupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer.ui.nav.Routes
import com.example.levelupgamer.ui.screens.CatalogScreen
import com.example.levelupgamer.ui.screens.LoginScreen
import com.example.levelupgamer.ui.screens.PointsScreen
import com.example.levelupgamer.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LevelUpGamerTheme {
                val vm: LevelUpViewModel = viewModel()
                val nav = rememberNavController()
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen {
                        showSplash = false
                    }
                } else {
                    Scaffold { _ ->
                        NavHost(
                            navController = nav,
                            startDestination = Routes.Catalog
                        ) {
                            // 🛒 Catálogo principal
                            composable(Routes.Catalog) {
                                CatalogScreen(
                                    vm,
                                    onGoPoints = { nav.navigate(Routes.Points) },
                                    onGoLogin = { nav.navigate(Routes.Login) }
                                )
                            }

                            // ⭐ Pantalla de puntos
                            composable(Routes.Points) {
                                PointsScreen(vm, onBack = { nav.popBackStack() })
                            }

                            // 🔐 Pantalla de login / registro
                            composable(Routes.Login) {
                                LoginScreen(vm, onBack = { nav.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000) // ⏳ espera 3 segundos
        onFinish()
    }

    // 🎨 Fondo igual al catálogo (negro gamer)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark), // 👈 tu color del tema
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 🎮 Logo principal
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo LevelUpGamer",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp)
            )

            // 💚 Nombre principal
            Text(
                text = "LEVEL-UP GAMER",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RedGamer, // 💚 color neón definido en tu tema
                letterSpacing = 2.sp
            )
        }
    }
}
