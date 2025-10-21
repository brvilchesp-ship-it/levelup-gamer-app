
package com.example.levelupgamer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
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

                var onPhotoPicked: (Uri?) -> Unit by remember { mutableStateOf({}) }
                val photoPicker = rememberLauncherForActivityResult(
                    ActivityResultContracts.PickVisualMedia()
                ) { uri -> onPhotoPicked(uri) }

                Scaffold { _ ->
                    NavHost(navController = nav, startDestination = Routes.Catalog) {
                        composable(Routes.Catalog) {
                            CatalogScreen(vm, onGoPoints = { nav.navigate(Routes.Points) },
                                onGoLogin = { nav.navigate(Routes.Login) })
                        }
                        composable(Routes.Points) {
                            PointsScreen(vm, onBack = { nav.popBackStack() })
                        }
                        composable(Routes.Login) {
                            LoginScreen(
                                vm,
                                onBack = { nav.popBackStack() },
                                onPickPhoto = {
                                    onPhotoPicked = it
                                    photoPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
