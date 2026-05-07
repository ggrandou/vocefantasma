package com.vocefantasma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.vocefantasma.R
import com.vocefantasma.viewmodel.MainViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Default.Home)
    object Catalogue : Screen("catalogue", "Catalogue", Icons.Default.List)
    object Settings : Screen("settings", "Paramètres", Icons.Default.Settings)
    object Info : Screen("info", "Informations", Icons.Default.Info)
}

@Composable
fun VoceFantasmaApp(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFDDDDDD),
            bottomBar = {
                NavigationBar(containerColor = Color.Black.copy(alpha = 0.5f)) {
                    val screens = listOf(Screen.Home, Screen.Catalogue, Screen.Settings, Screen.Info)
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen(viewModel)
                    Screen.Catalogue -> CatalogueScreen(viewModel)
                    Screen.Settings -> SettingsScreen(viewModel)
                    Screen.Info -> InfoScreen()
                }
            }
        }
    }
}
