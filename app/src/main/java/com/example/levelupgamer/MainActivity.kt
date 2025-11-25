package com.example.levelupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.levelupgamer.remote.ProductRetrofitInstance   // 👈🔥 IMPORT IMPORTANTE
import com.example.levelupgamer.ui.screens.CatalogScreen
import com.example.levelupgamer.ui.screens.LoginScreen
import com.example.levelupgamer.ui.screens.PointsScreen
import com.example.levelupgamer.ui.screens.ExternalPostScreen
import com.example.levelupgamer.ui.theme.BackgroundDark
import com.example.levelupgamer.ui.theme.LevelUpGamerTheme
import com.example.levelupgamer.ui.theme.RedGamer
import com.example.levelupgamer.viewmodel.LevelUpViewModel
import com.example.levelupgamer.viewmodel.ExternalPostViewModel
import com.example.levelupgamer.viewmodel.ExternalPostViewModelFactory
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LevelUpGamerTheme {

                val vm: LevelUpViewModel = viewModel()
                val nav = rememberNavController()

                // 🔥 ViewModel para la pantalla de posts externos
                val externalVm: ExternalPostViewModel = viewModel(
                    factory = ExternalPostViewModelFactory()
                )


                var showSplash by remember { mutableStateOf(true) }
                var userName by remember { mutableStateOf("") }
                var hasDiscount by remember { mutableStateOf(false) }

                if (showSplash) {
                    SplashScreen { showSplash = false }
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = BackgroundDark
                    ) { inner ->

                        NavHost(
                            navController = nav,
                            startDestination = "catalog",
                            modifier = Modifier.padding(inner)
                        ) {

                            // 📌 Catálogo
                            composable("catalog") {
                                CatalogScreen(
                                    vm = vm,
                                    userName = userName,
                                    hasDiscount = hasDiscount,
                                    onGoPoints = { nav.navigate("points") },
                                    onGoLogin = { nav.navigate("login") },
                                    onGoExternalPosts = { nav.navigate("external_posts") }
                                )
                            }

                            // 📌 Puntos
                            composable("points") {
                                PointsScreen(
                                    vm = vm,
                                    onBack = { nav.popBackStack() }
                                )
                            }

                            // 📌 Login
                            composable("login") {
                                LoginScreen(
                                    vm = vm,
                                    onBack = { nav.popBackStack() },
                                    onSuccessLogin = { name, duoc ->
                                        userName = name
                                        hasDiscount = duoc
                                        nav.navigate("catalog") {
                                            popUpTo("catalog") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            // 📌 🔥 Nueva pantalla: Posts externos (API)
                            composable("external_posts") {
                                ExternalPostScreen(
                                    viewModel = externalVm,
                                    onBack = { nav.popBackStack() }
                                )
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
        delay(2000)
        onFinish()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "LEVEL-UP GAMER",
                color = RedGamer,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
