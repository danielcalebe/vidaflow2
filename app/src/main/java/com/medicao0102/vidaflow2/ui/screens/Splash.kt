package com.medicao0102.vidaflow2.ui.screens

import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.medicao0102.vidaflow2.Loading
import com.medicao0102.vidaflow2.R
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.data.LoginResponse
import com.medicao0102.vidaflow2.data.StatusResponse
import com.medicao0102.vidaflow2.data.TipResponse
import com.medicao0102.vidaflow2.data.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Splash(navController: NavHostController, apiService: ApiService, ctx: Context) {

  var isLoading by remember { mutableStateOf(false) }
  var showDialog by remember { mutableStateOf(false) }
  var isStatusAvailable by remember { mutableStateOf<Boolean?>(null) }
  var getLoginResponse by remember { mutableStateOf<LoginResponse?>(null) }
  val scope = rememberCoroutineScope()





  LaunchedEffect(Unit) {
    isLoading = true
    delay(200)
    isStatusAvailable = apiService.isStatusAvailable()
    if (isStatusAvailable == true) {
      getLoginResponse = apiService.getLoginResponse()
      getLoginResponse?.let {
        val result = apiService.getTip()
        delay(3000);
        when (result) {
          is UiState.Error -> {
            navController.navigate("login")
          }

          UiState.Loading -> {}
          is UiState.Success<TipResponse> -> {
            navController.navigate("home")
          }
        }
      } ?: navController.navigate("login")
    } else {
      showDialog = true;
    }
    isLoading = false

  }


  Column(
    Modifier
      .fillMaxSize()
      .clickable {

        scope.launch {
          if (isStatusAvailable == true) {
            getLoginResponse = apiService.getLoginResponse()
            getLoginResponse?.let {
              val result = apiService.getTip()
              when (result) {
                is UiState.Error -> {
                  navController.navigate("login")
                }

                UiState.Loading -> {}
                is UiState.Success<TipResponse> -> {
                  navController.navigate("home")
                }
              }
            } ?: navController.navigate("login")
          } else {
            showDialog = true;
          }
          isLoading = false
        }
      }
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center) {

    Image(
      painter = painterResource(R.drawable.logo),
      null,
      modifier = Modifier.fillMaxWidth(0.5f),
      contentScale = ContentScale.Crop
    )

    Column(
      Modifier.fillMaxWidth(0.8f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text("Cuide de você, um hábito de cada vez.", fontStyle = FontStyle.Italic)
      AnimatedVisibility(isLoading) {
        Loading()
      }
      if (showDialog) Dialog(
        onDismissRequest = {}) {
        Column(
          Modifier
            .clip(RoundedCornerShape(12))
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
        ) {
          Text(
            "Erro ao se conectar ao servidor. Tente novamente.",
            color = MaterialTheme.colorScheme.error
          )
          Spacer(Modifier.height(24.dp))
          Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
          ) {
            TextButton(onClick = {
              (ctx as? Activity)?.finish()
            }) { Text("Sair") }
            Button(onClick = {
              scope.launch {
                showDialog = false
                isLoading = true
                delay(300)
                isStatusAvailable = apiService.isStatusAvailable()
                if (isStatusAvailable == true) {
                  delay(3000); navController.navigate("login")
                } else {
                  showDialog = true;
                }
                isLoading = false
              }
            }) { Text("Tentar novamente") }
          }
        }
      }
    }

  }


}