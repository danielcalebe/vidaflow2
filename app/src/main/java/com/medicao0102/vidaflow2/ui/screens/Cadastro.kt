package com.medicao0102.vidaflow2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.medicao0102.vidaflow2.Loading
import com.medicao0102.vidaflow2.R
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.data.LoginRequest
import com.medicao0102.vidaflow2.data.LoginResponse
import com.medicao0102.vidaflow2.data.UiState
import kotlinx.coroutines.launch

@Composable

fun Login(apiService: ApiService, navController: NavHostController, sh: SnackbarHostState) {
  var email by remember { mutableStateOf("") }
  var pass by remember { mutableStateOf("") }
  var showPass by remember { mutableStateOf(false) }
  var result by remember { mutableStateOf<UiState<LoginResponse>?>(null) }
  val scope = rememberCoroutineScope()
  Column(
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {

    Image(
      painter = painterResource(R.drawable.logo),
      null,
      modifier = Modifier.fillMaxWidth(0.4f),
      contentScale = ContentScale.Crop
    )

    Column(
      Modifier
        .fillMaxHeight()
        .fillMaxWidth(0.7f),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        trailingIcon = { Icon(Icons.Default.Email, null) },
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = pass,
        onValueChange = { pass = it },
        label = { Text("Senha") },
        trailingIcon = {
          IconButton(onClick = {
            showPass = !showPass
          }) {
            if (!showPass) {
              Icon(
                Icons.Default.Visibility, null
              )

            } else {
              Icon(Icons.Default.VisibilityOff, null)
            }
          }
        },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
      )

      Spacer(Modifier.height(8.dp))

      Button(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          sh.currentSnackbarData?.dismiss()
          result = UiState.Loading
          scope.launch {
            result = apiService.login(
              LoginRequest(
                email,
                pass
              )
            )
            result?.let {
              when (it) {
                is UiState.Error -> {
                  sh.showSnackbar(it.errorResponse.message, duration = SnackbarDuration.Indefinite)
                }

                UiState.Loading -> {

                }

                is UiState.Success<LoginResponse> -> {
                  navController.navigate("home")
                }
              }


            }
          }
        },
        enabled = email.isNotEmpty() && pass.isNotEmpty()
      ) {
        Text("Entrar")
      }

        TextButton(
          onClick =
            {
              navController.navigate("cadastro")
            }) {
          Text("Não tem conta? Cadastre-se.", color = MaterialTheme.colorScheme.primary)
        }

      AnimatedVisibility(result is UiState.Loading) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          Loading()
        }
      }
    }


  }


}