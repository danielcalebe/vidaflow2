package com.medicao0102.vidaflow2

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets.Type
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.ui.screens.Cadastro
import com.medicao0102.vidaflow2.ui.screens.CriarHabito
import com.medicao0102.vidaflow2.ui.screens.Home
import com.medicao0102.vidaflow2.ui.screens.Login
import com.medicao0102.vidaflow2.ui.screens.Splash
import com.medicao0102.vidaflow2.ui.theme.Vidaflow2Theme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val navController = rememberNavController()
      val navBackStackEntry = navController.currentBackStackEntryAsState()
      val currentRoute = navBackStackEntry.value?.destination?.route


      val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

      val context = LocalContext.current

      val apiService = remember { ApiService(context) }


      if (currentRoute == "splash") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          windowInsetsController.hide(Type.statusBars())
        }
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          windowInsetsController.show(Type.statusBars())
        }
      }

      val sh = remember { SnackbarHostState() }


      Vidaflow2Theme {

        Scaffold(
          floatingActionButton = {
            if (currentRoute == "home")
              FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate("criar_habito") }
              ) {
                Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
              }
          },
          bottomBar = {
            if (currentRoute == "home" || currentRoute == "historico" || currentRoute == "perfil") {
              NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
              ) {
                NavigationBarItem(
                  selected = currentRoute == "home",
                  onClick = { navController.navigate("home") },
                  icon = {
                    Icon(
                      Icons.Default.Dashboard,
                      null,
                      tint = if (currentRoute == "home") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary
                  ),
                  label = {
                    Text(
                      "Hábitos",
                      color = if (currentRoute == "home") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                )

                NavigationBarItem(
                  selected = currentRoute == "historico",
                  onClick = { navController.navigate("historico") },
                  icon = {
                    Icon(
                      Icons.Default.History,
                      null,
                      tint = if (currentRoute == "historico") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary
                  ),
                  label = {
                    Text(
                      "Histórico",
                      color = if (currentRoute == "historico") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                )

                NavigationBarItem(
                  selected = currentRoute == "perfil",
                  onClick = { navController.navigate("perfil") },
                  icon = {
                    Icon(
                      Icons.Default.Person,
                      null,
                      tint = if (currentRoute == "perfil") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary
                  ),
                  label = {
                    Text(
                      "Perfil",
                      color = if (currentRoute == "perfil") Color(0xFFCEB85A) else MaterialTheme.colorScheme.background
                    )
                  },
                )

              }
            }
          }, snackbarHost = {

            SnackbarHost(sh)
          }, modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          Column(
            modifier = Modifier.padding(innerPadding)
          ) {
            NavHost(
              navController,
              "splash"
            ) {
              composable("splash") {
                Splash(navController, apiService, context)
              }
              composable("login") {
                Login(apiService, navController, sh)

              }
              composable("cadastro") {
                Cadastro(apiService, navController, sh)

              }
              composable("home") {
                Home(apiService, navController, context)
              }
              composable("criar_habito") {
                CriarHabito(apiService,navController, sh)
              }
            }
          }
        }
      }
    }
  }
}


@Composable
fun Loading() {
  CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
}