package com.medicao0102.vidaflow2.ui.screens

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicao0102.vidaflow2.Loading
import com.medicao0102.vidaflow2.data.ApiService
import com.medicao0102.vidaflow2.data.HabitResponse
import com.medicao0102.vidaflow2.data.HabitStatus
import com.medicao0102.vidaflow2.data.LoginResponse
import com.medicao0102.vidaflow2.data.TipResponse
import com.medicao0102.vidaflow2.data.UiState
import com.medicao0102.vidaflow2.ui.screens.PERIODOS.MADRUGADA
import com.medicao0102.vidaflow2.ui.screens.PERIODOS.MANHA
import com.medicao0102.vidaflow2.ui.screens.PERIODOS.NOITE
import com.medicao0102.vidaflow2.ui.screens.PERIODOS.TARDE
import io.ktor.http.hostIsIp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import java.time.LocalTime
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Home(apiService: ApiService, navController: NavHostController, ctx: Context) {

  var userData by remember { mutableStateOf<LoginResponse?>(null) }
  var habits by remember { mutableStateOf<UiState<List<HabitResponse>>?>(null) }
  var habitsJson by remember { mutableStateOf<List<HabitResponse>?>(null) }
  var scope = rememberCoroutineScope()
  var isDoneSize by remember { mutableStateOf(0) }
  var stateDonePercentage by remember { mutableStateOf(0f) }
  var dica by remember { mutableStateOf<UiState<TipResponse>?>(null) }

  LaunchedEffect(Unit) {
    userData = apiService.getLoginResponse()
    habits = apiService.getHabit(userData?.userId.toString())
    Log.d("habitssss", habits.toString())
    if (habits is UiState.Error) {
      habitsJson = try {
        Json.decodeFromString<List<HabitResponse>>(
          ctx.assets.open("habits.json").bufferedReader().use { it.readText() })
      } catch (e: Exception) {
        null
      }
    }
    isDoneSize =
      apiService.getHabitStatusList()?.let { list -> list.filter { !it.isDone }.size } ?: 0

    stateDonePercentage = apiService.getHabitStatusList()?.let { list ->
      list.filter { it.isDone }.size.toFloat() / list.size.toFloat()
    } ?: 0f

    dica = apiService.getTip()
  }



  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
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
        }, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
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
                  containerColor = MaterialTheme.colorScheme.secondary,
                  contentColor = MaterialTheme.colorScheme.background
                ) {
                  Text(isDoneSize.toString())
                }
              }) {

              Icon(
                Icons.Default.Notifications, null, modifier = Modifier.clickable {
                  Toast.makeText(
                    ctx, "Você tem $isDoneSize hábito(s) para completar hoje!", Toast.LENGTH_SHORT
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
                color = when (stateDonePercentage * 100) {
                  in 0f..40f -> Color(0xFFEF4444)
                  in 41f..70f -> Color(0xFFFF9800)
                  in 71f..100f -> Color(0xFF1A7F64)
                  else -> Color(0xFFFFFFFF)
                },
                -90f,
                360 * stateDonePercentage,
                useCenter = false,
                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round)
              )

            }


            Text(
              "${(stateDonePercentage * 100).toInt()}%",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )


          }
          Spacer(Modifier.height(8.dp))

          Text(
            "${

              apiService.getHabitStatusList()?.filter { it.isDone }?.size ?: 0
            } de ${
              habits?.let {
                if (it is UiState.Success) it.result.size
                else apiService.getHabitStatusList()?.size ?: 0
              }
            } hábitos concluídos hoje")
        }
      }
    }


    habits?.let {
      when (it) {
        is UiState.Error -> {
          Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Text(it.errorResponse.message, color = MaterialTheme.colorScheme.error)
          }
        }

        UiState.Loading -> {
          Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
          ) {
            Loading()
          }

        }

        is UiState.Success<List<HabitResponse>> -> {
          var isRefreshing by remember { mutableStateOf(false) }

          Column(Modifier.fillMaxWidth()) {
            Row(
              Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 24.dp)
            ) {
              Text(
                "Seus hábitos hoje",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
              )
            }
            PullToRefreshBox(
              isRefreshing = isRefreshing,
              onRefresh = {
                isRefreshing = true
                scope.launch {
                  habits = apiService.getHabit(userData?.userId.toString())
                  if (habits is UiState.Error) habitsJson = try {
                    Json.decodeFromString<List<HabitResponse>>(
                      ctx.assets.open("habits.json").bufferedReader().use { it.readText() })
                  } catch (e: Exception) {
                    null
                  }
                  delay(200)
                  isRefreshing = false
                }
              },

              ) {
//                        var sorted = remember { mutableStateListOf<HabitResponse?>(null) }
//                        sorted.addAll(
//                            it.result.sortedBy { item ->
//                                apiService.getHabitStatusList()?.let { list ->
//                                    val a = list.filter { it.isDone }.map { it.habitId }
//                                    a.contains(item.id)
//                                }
//                            })


              LazyColumn(
                modifier = Modifier
                  .padding(24.dp)
                  .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {


//                            Log.d("groupo", sorted.toString())

                items(it.result) { item ->


                  var showBottomSheet by remember { mutableStateOf(false) }
                  var bottomSheetData by remember { mutableStateOf<HabitResponse?>(null) }
                  var sheetState = rememberModalBottomSheetState()
                  if (showBottomSheet) {
                    ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
                      showBottomSheet = false
                    }) {
                      Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                      ) {
                        Row(
                          Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                          TextButton(onClick = {
                              navController.navigate("criar_editar_habito/${Json.encodeToString(item)}")
                          }) {
                            Text("Editar")
                          }
                          VerticalDivider(Modifier.height(12.dp), color = MaterialTheme.colorScheme.secondary)
                          TextButton(onClick = {

                          }) {
                            Text("Duplicar", color = MaterialTheme.colorScheme.surfaceDim)
                          }
                          VerticalDivider(Modifier.height(12.dp), color = MaterialTheme.colorScheme.secondary)
                          TextButton(onClick = {

                          }) {
                            Text("Excluir", color = MaterialTheme.colorScheme.error)
                          }
                          VerticalDivider(Modifier.height(12.dp), color = MaterialTheme.colorScheme.secondary)
                          TextButton(onClick = {

                          }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                          }
                        }
                      }
                    }
                  }
                  var offsetX by remember { mutableFloatStateOf(0f) }
                  Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                      .fillMaxWidth()
                      .combinedClickable(onClick = {}, onLongClick = {
                        showBottomSheet = true
                      }),
//                    .offset { IntOffset(offsetX.roundToInt(), 0) }
//                    .draggable(
//                      orientation = Orientation.Horizontal,
//                      state = rememberDraggableState { delta -> offsetX += delta }),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)

                  ) {


                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                      Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {


                        if (apiService.getHabitStatus(
                            item.id
                          ) == null
                        ) apiService.putHabitSatus(
                          HabitStatus(
                            item.id, false
                          )
                        )

                        var status by remember {
                          mutableStateOf(
                            apiService.getHabitStatus(item.id) ?: HabitStatus(
                              item.id, false
                            )
                          )
                        }


                        IconButton(onClick = {

                          status = status.copy(isDone = !status.isDone)
                          apiService.putHabitSatus(
                            HabitStatus(
                              item.id, status.isDone
                            )
                          )




                          stateDonePercentage = apiService.getHabitStatusList()?.let { list ->
                            list.filter { it.isDone }.size.toFloat() / list.size.toFloat()
                          } ?: 0f


                          isDoneSize = apiService.getHabitStatusList()
                            ?.let { list -> list.filter { !it.isDone }.size } ?: 0
                        }) {
                          if (!status.isDone) Icon(
                            Icons.Default.CheckBoxOutlineBlank, null
                          )
                          else Icon(
                            Icons.Default.CheckBox, null, tint = MaterialTheme.colorScheme.primary
                          )
                        }
                        Icon(
                          when (item.categoria) {
                            CATEGORIAS.Hidratação.name -> {
                              Icons.Default.WaterDrop
                            }

                            CATEGORIAS.Exercício.name -> {
                              Icons.Default.FitnessCenter
                            }

                            CATEGORIAS.Alimentação.name -> {
                              Icons.Default.Fastfood
                            }

                            CATEGORIAS.Sono.name -> {

                              Icons.Default.Bed
                            }

                            CATEGORIAS.Saúde.name -> {

                              Icons.Default.HealthAndSafety
                            }

                            else -> {

                              Icons.Default.More
                            }
                          }, null, tint = MaterialTheme.colorScheme.secondary
                        )

                        Column() {
                          Text(
                            item.nome, style = MaterialTheme.typography.titleLarge.copy(
                              fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                            )
                          )
                          Row(verticalAlignment = Alignment.CenterVertically) { }
                          Text(
                            item.categoria + " - " + item.horario_alvo,
                            style = MaterialTheme.typography.labelSmall.copy(
                              fontWeight = FontWeight.Medium
                            )
                          )

                        }

                      }
                    }
                  }
                }


              }


            }


            Column(
              Modifier
                .fillMaxWidth()
                .padding(24.dp)
            ) {

              Text(
                "Dica do dia", style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
              )
              Spacer(Modifier.height(4.dp))

              Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                  containerColor = MaterialTheme.colorScheme.primary.copy(0.2f)
                )
              ) {


                dica?.let { tip ->
                  when (tip) {
                    is UiState.Error -> {
                      Column(
                        Modifier
                          .fillMaxWidth()
                          .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                      ) {
                        Icon(
                          Icons.Default.Lightbulb, null, tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Beba água regularmente ")
                        Box(
                          Modifier
                            .clip(
                              RoundedCornerShape(12.dp)
                            )
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(vertical = 4.dp, horizontal = 8.dp)


                        ) {
                          Text("Saude")
                        }

                      }
                    }

                    UiState.Loading -> {
                      Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                      ) {
                        Loading()
                      }
                    }

                    is UiState.Success<TipResponse> -> {
                      Column(
                        Modifier
                          .fillMaxWidth()
                          .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                      ) {
                        Icon(
                          Icons.Default.Lightbulb, null, tint = MaterialTheme.colorScheme.primary
                        )
                        Text(tip.result.texto)
                        Box(
                          Modifier
                            .clip(
                              RoundedCornerShape(12.dp)
                            )
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(vertical = 4.dp, horizontal = 8.dp)

                        ) {
                          Text(tip.result.categoria)
                        }
                      }
                    }
                  }
                }
              }
            }
          }
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

enum class CATEGORIAS {
  Saúde, Sono, Hidratação, Exercício, Alimentação, Outro
}
