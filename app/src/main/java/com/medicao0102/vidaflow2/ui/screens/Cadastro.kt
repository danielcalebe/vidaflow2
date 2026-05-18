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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
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
import com.medicao0102.vidaflow2.data.RegisterRequest
import com.medicao0102.vidaflow2.data.RegisterResponse
import com.medicao0102.vidaflow2.data.UiState
import com.medicao0102.vidaflow2.ui.theme.Poppins
import kotlinx.coroutines.launch

@Composable

fun Cadastro(apiService: ApiService, navController: NavHostController, sh: SnackbarHostState) {
  var nome by remember { mutableStateOf("") }
  var nomeErr by remember { mutableStateOf<String?>(null) }

  var email by remember { mutableStateOf("") }
  var emailErr by remember { mutableStateOf<String?>(null) }

  var pass by remember { mutableStateOf("") }
  var passErr by remember { mutableStateOf<String?>(null) }

  var passConfirm by remember { mutableStateOf("") }
  var passConfirmErr by remember { mutableStateOf<String?>(null) }


  var showPass by remember { mutableStateOf(false) }
  var showPassConfirm by remember { mutableStateOf(false) }
  var result by remember { mutableStateOf<UiState<RegisterResponse>?>(null) }
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

      Column() {
        OutlinedTextField(
          value = nome,
          onValueChange = { nome = it },
          label = { Text("Nome completo") },
          trailingIcon = { Icon(Icons.Default.Person, null) },
          modifier = Modifier.fillMaxWidth()
        )
      }

      Column() {
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it; if (
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
          ) {
            emailErr = "Formato de email inválido"
          } else {
            emailErr = null
          }
          },
          label = { Text("Email") },
          trailingIcon = { Icon(Icons.Default.Email, null) },
          modifier = Modifier.fillMaxWidth()
        )

        emailErr?.let {
          Text(
            emailErr!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = Poppins)
          )
        }
      }
      Column() {
        OutlinedTextField(
          value = pass,
          onValueChange = {
            pass = it; if (pass.length < 6) passErr = "Mínimo 6 caracteres" else passErr = null
          },
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

        passErr?.let {
          Text(
            passErr!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = Poppins)
          )
        }
      }

      Column() {
        OutlinedTextField(
          value = passConfirm,
          onValueChange = {
            passConfirm = it; if (pass != passConfirm) passConfirmErr =
            "As senhas não coincidem" else passConfirmErr = null
          },
          label = { Text("Confirmar senha") },
          trailingIcon = {
            IconButton(onClick = {
              showPassConfirm = !showPassConfirm
            }) {
              if (!showPassConfirm) {
                Icon(
                  Icons.Default.Visibility, null
                )

              } else {
                Icon(Icons.Default.VisibilityOff, null)
              }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          visualTransformation = if (showPassConfirm) VisualTransformation.None else PasswordVisualTransformation()
        )

        passConfirmErr?.let {
          Text(
            passConfirmErr!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = Poppins)
          )
        }
      }

      Spacer(Modifier.height(8.dp))

      Button(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {

          sh.currentSnackbarData?.dismiss()



          result = UiState.Loading
          scope.launch {
            if (pass == passConfirm && emailErr == null && passErr == null && passErr == null && nomeErr == null) {
              result = apiService.register(
                RegisterRequest(
                  email, nome, pass
                )
              )
              result?.let {
                when (it) {
                  is UiState.Error -> {
                    sh.showSnackbar(
                      it.errorResponse.message,
                      duration = SnackbarDuration.Indefinite
                    )
                  }

                  UiState.Loading -> {

                  }

                  is UiState.Success<RegisterResponse> -> {
                    sh.showSnackbar("Conta criada com sucesso! Faça login.")
                    navController.navigate("login")
                  }
                }
              }

            }
            else {
              result = null
             sh.showSnackbar("Erro verifique os campos!", duration = SnackbarDuration.Short)
            }
          }
        },
        enabled = email.isNotEmpty() && pass.isNotEmpty() && passConfirm.isNotEmpty() && nome.isNotEmpty()
      ) {
        Text("Criar conta")
      }

      TextButton(
        onClick =
          {
            navController.navigate("login")
          }) {
        Text("Já possui uma conta? Entre agora.", color = MaterialTheme.colorScheme.primary)
      }

      AnimatedVisibility(result is UiState.Loading) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          Loading()
        }
      }
    }


  }


}