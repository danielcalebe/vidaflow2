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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.ui.screens.Cadastro
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


      if (currentRoute == "splash"){
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

          Scaffold(snackbarHost = {
            SnackbarHost(sh)
          },modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                  Home()
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