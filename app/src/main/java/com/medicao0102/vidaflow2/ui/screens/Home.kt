package com.medicao0102.vidaflow2.ui.screens

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.medicao0102.vidaflow2.Loading
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.data.HabitResponse
import com.medicao0102.vidaflow2.data.LoginResponse
import com.medicao0102.vidaflow2.data.UiState
import com.medicao0102.vidaflow2.ui.screens.PERIODOS.*
import java.time.Duration
import java.time.LocalTime

@Composable
fun Home(apiService: ApiService, navController: NavHostController, ctx: Context) {

    var userData by remember { mutableStateOf<LoginResponse?>(null) }
    var habits by remember { mutableStateOf<UiState<List<HabitResponse>>?>(null) }
    LaunchedEffect(Unit) {
        userData = apiService.getLoginResponse()
        habits = apiService.getHabit(userData?.userId.toString())
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        Button(onClick ={
//
//
//            navController.navigate("login")
//        } ) {
//        }

        val period = remember { getPeriod() }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                when (period) {
                    MANHA -> {
                        "Bom dia, ${userData?.name}"
                    }

                    TARDE -> {

                        "Boa tarde, ${userData?.name}"
                    }

                    NOITE, MADRUGADA -> {

                        "Boa noite, ${userData?.name}"
                    }
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(onClick = {

                }) {
                    habits?.let {
                        if (it is UiState.Success) BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.background
                                ) {
                                    Text(it.result.size.toString())
                                }
                            }) {

                            Icon(
                                Icons.Default.Notifications, null, modifier = Modifier.clickable {
                                    Toast.makeText(
                                        ctx,
                                        "Você tem ${it.result.size} hábito(s) para completar hoje!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })

                        }

                        Icon(Icons.Default.Notifications, null)
                    }
                }

                IconButton(
                    onClick = {
                        navController.navigate("perfil")
                    }) {
                    Icon(Icons.Default.Settings, null)
                }
            }


        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(24.dp), horizontalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.primary.copy(0.2f)
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {

                        Canvas(
                            modifier = Modifier.size(100.dp)
                        ) {
                            drawArc(
                                color = Color.Gray,
                                -90f,
                                360 * 1f,
                                useCenter = false,
                                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round)
                            )

                            drawArc(
                                color = Color(0xFF1A7F64),
                                -90f,
                                360 * 0.5f,
                                useCenter = false,
                                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round)
                            )

                        }


                        Text(
                            "14%",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )


                    }
                }
            }
        }



        habits?.let {
            when (it) {
                is UiState.Error -> {
                    Text("Erro")
                }

                UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Loading()
                    }

                }

                is UiState.Success<List<HabitResponse>> -> {
                    LazyColumn(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        items(it.result) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row() {
                                    Text(it.nome)
                                }
                            }
                        }
                    }
                }

                null -> {

                }
            }
        }
    }
}


fun getPeriod(): PERIODOS {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val horaAtual = LocalTime.now().hour
        when (horaAtual) {
            in 5..11 -> return PERIODOS.MANHA
            in 12..17 -> return PERIODOS.TARDE
            in 18..23 -> return PERIODOS.NOITE
            else -> return PERIODOS.MADRUGADA
        }
    } else {
        return PERIODOS.MANHA
    }
}


enum class PERIODOS {
    MANHA, TARDE, NOITE, MADRUGADA
}