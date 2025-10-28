package com.example.levelupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer.ui.nav.Routes
import com.example.levelupgamer.ui.screens.CatalogScreen
import com.example.levelupgamer.ui.screens.LoginScreen
import com.example.levelupgamer.ui.screens.PointsScreen
import com.example.levelupgamer.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LevelUpGamerTheme {
                val vm: LevelUpViewModel = viewModel()
                val nav = rememberNavController()

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
                            PointsScreen(
                                vm,
                                onBack = { nav.popBackStack() }
                            )
                        }

                        // 🔐 Pantalla de login / registro
                        composable(Routes.Login) {
                            LoginScreen(
                                vm,
                                onBack = { nav.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
