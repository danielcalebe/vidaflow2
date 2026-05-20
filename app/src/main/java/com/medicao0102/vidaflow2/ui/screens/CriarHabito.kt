package com.medicao0102.vidaflow2.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicao0102.vidaflow2.R
import com.medicao0102.vidaflow2.data.ApiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarHabito(apiService: ApiService, navController: NavHostController, sh: SnackbarHostState) {

  var nomeErr by remember { mutableStateOf<String?>(null) }
  var descricaoErr by remember { mutableStateOf<String?>(null) }
  var categoriaErr by remember { mutableStateOf<String?>(null) }
  var scope = rememberCoroutineScope()

  var nome by remember { mutableStateOf("") }
  var descricao by remember { mutableStateOf("") }
  var tagInput by remember { mutableStateOf("") }
  val tagList = remember { mutableStateListOf<String>() }

  var horario by remember { mutableStateOf("") }
  var horarioExpanded by remember { mutableStateOf(false) }
  var currentTime = Calendar.getInstance()
  var timePickerState = rememberTimePickerState(
    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
    initialMinute = currentTime.get(Calendar.MINUTE),
    is24Hour = true
  )
  var categoriaExpanded by remember { mutableStateOf(false) }
  var selectedCategoria by remember { mutableStateOf("") }
  val diasSemana = listOf<String>(
    "Seg",
    "Ter",
    "Qua",
    "Qui",
    "Sex",
    "Sab",
    "Dom"
  )
  val selectedDiasSemana = remember { mutableStateListOf<String>() }


  val frequenciaList = listOf<String>(
    "Diário",
    "Dias de semana",
    "Fins de semana",
    "Personalizado"
  )
  var selectedFrequencia by remember { mutableStateOf("") }



  Column(
    Modifier
      .verticalScroll(rememberScrollState())
      .background(MaterialTheme.colorScheme.background)
  ) {
    Row(
      Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      IconButton({
        navController.popBackStack()
      }) {
        Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
      }
      Text(
        "Novo Hábito",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
      )
      Spacer(Modifier.weight(1f))
      Image(
        painter = painterResource(R.drawable.logo),
        null,
        modifier = Modifier.size(60.dp),
        contentScale = ContentScale.Crop
      )


    }


    Column(
      Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

      Column(
        Modifier
          .fillMaxWidth()
          .background(
            brush =
              Brush.horizontalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primary,
                  MaterialTheme.colorScheme.secondary
                )
              )
          )
          .padding(12.dp)
      ) {
        Text(
          "Adicione um novo hábito à sua lista",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
          color = MaterialTheme.colorScheme.background
        )
      }
      OutlinedTextField(
        value = nome,
        onValueChange = {
          nome = it
          nomeErr =
            if (nome.isEmpty()) "Campo obrigatório"
            else if (nome.length < 3) "Mínimo 3 caracteres"
            else if (nome.length > 60) "Máximo 60 caracteres"
            else null
        },
        label = { Text("Nome do hábito") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
          Icon(Icons.Default.Dashboard, null, tint = MaterialTheme.colorScheme.secondary)
        }
      )


      Row(Modifier.fillMaxWidth()) {

        nomeErr?.let {
          Text(
            nomeErr!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      OutlinedTextField(
        isError = descricaoErr != null,
        value = descricao,
        onValueChange = {
          descricao = it
          descricaoErr =
            if (descricao.length > 200) "Máximo 200 caracteres"
            else null
        },
        label = { Text("Descrição") },
        minLines = 3,
        singleLine = false,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
          Icon(Icons.Default.Description, null, tint = MaterialTheme.colorScheme.secondary)
        }

      )

      Row(Modifier.fillMaxWidth()) {
        if (descricao.length in 1..200) {
          Text(
            "Restam ${200 - descricao.length} caracteres",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium
            )
          )

        }
        descricaoErr?.let {
          Text(
            descricaoErr!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ExposedDropdownMenuBox(
          expanded = categoriaExpanded,
          onExpandedChange = {},
          modifier = Modifier.weight(1f)
        ) {
          Column() {
            OutlinedTextField(
              isError = categoriaErr != null,
              value = selectedCategoria,
              onValueChange = {
                categoriaErr = if (selectedCategoria.isEmpty()) "Campo obrigatório" else null
              },
              modifier = Modifier.fillMaxWidth(),
              label = { Text("Categoria") },
              singleLine = true,

              trailingIcon = {

                IconButton(onClick = {
                  categoriaExpanded = !categoriaExpanded
                }) {
                  ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded)
                }
              }

            )
            categoriaErr?.let {
              Text(
                categoriaErr!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }

          DropdownMenu(
            expanded = categoriaExpanded,
            onDismissRequest = { categoriaExpanded = false },
            containerColor = MaterialTheme.colorScheme.background
          ) {
            CATEGORIAS.values().forEach {
              DropdownMenuItem(
                text = { Text(it.name) },
                onClick = { selectedCategoria = it.name; categoriaExpanded = false }
              )
            }
          }
        }



        OutlinedTextField(
          horario,
          {},
          readOnly = true,
          label = { Text("Horário alvo") },
          trailingIcon = {
            IconButton(onClick = {
              horarioExpanded = !horarioExpanded
            }) {
              Icon(Icons.Default.HourglassTop, null, tint = MaterialTheme.colorScheme.secondary)
            }
          },
          modifier = Modifier.weight(1f)
        )



        if (horarioExpanded) {
          AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
              Button(
                onClick = {

                  horario =
                    if (timePickerState.hour < 10) {
                      "0${timePickerState.hour}"
                    } else {
                      timePickerState.hour.toString()

                    } + ":${timePickerState.minute}"
                  horarioExpanded = false
                }

              ) { Text("Salvar") }
            },
            dismissButton = {
              TextButton(onClick = {

                horarioExpanded = false
              }) {
                Text("Cancelar")
              }
            },
            text = {
              TimePicker(
                colors = TimePickerDefaults.colors(
                  timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary.copy(0.6f),

                  ),

                state = timePickerState
              )
            }
          )

        }
      }


      Spacer(Modifier.height(8.dp))
      Column(Modifier.fillMaxWidth()) {


        Text(
          "Frequência",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
          )
        )

        LazyRow(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(
            frequenciaList
          ) {

            FilterChip(
              selected = selectedFrequencia == it,
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.2f),
              ),
              onClick = {
                selectedFrequencia = it
              },
              label = { Text(it) }
            )
          }
        }


        if (selectedFrequencia == "Personalizado") {
          LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            items(diasSemana) { dia ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
              ) {

                Checkbox(
                  checked = selectedDiasSemana.contains(dia),
                  onCheckedChange = {
                    if (selectedDiasSemana.contains(dia)) selectedDiasSemana.remove(dia)
                    else selectedDiasSemana.add(dia)
                  }
                )
                Text(dia)
              }
            }
          }
        }


      }


      Column(Modifier.fillMaxWidth()) {
        var isLembreteActive by remember { mutableStateOf(false) }
        var selectedLembrete by remember { mutableStateOf("") }
        var lembreteList = listOf<String>(
          "5 min", "15 min", "30 min", "1h"
        )
        var expandedLembrete by remember { mutableStateOf(false) }
        Text(
          "Lembrete",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
          )
        )
        Switch(
          checked = isLembreteActive,
          onCheckedChange = { isLembreteActive = !isLembreteActive }
        )

        if (isLembreteActive)
          ExposedDropdownMenuBox(
            expanded = expandedLembrete,
            onExpandedChange = { },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = selectedLembrete,
              onValueChange = {},
              readOnly = true,
              label = {
                Text("Antecedência do lembrete")
              },
              modifier = Modifier.fillMaxWidth(),
              trailingIcon = {
                IconButton(onClick = {
                  expandedLembrete = !expandedLembrete
                }) {
                  ExposedDropdownMenuDefaults.TrailingIcon(expandedLembrete)
                }
              }
            )

            DropdownMenu(
              expanded = expandedLembrete,
              onDismissRequest = { expandedLembrete = false },

              containerColor = MaterialTheme.colorScheme.background
            ) {
              lembreteList.forEach {
                DropdownMenuItem(
                  text = { Text(it) },
                  onClick = { selectedLembrete = it; expandedLembrete = false }
                )
              }
            }

          }
      }


      var metaDiaria by remember { mutableStateOf("") }
      OutlinedTextField(
        value = metaDiaria,

        onValueChange = {
          it.toIntOrNull()?.let { a ->
            if (a > 0) metaDiaria = it
          } ?: ""
        },
        label = { Text("Meta diária") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
          Icon(Icons.Default.Today, null, tint = MaterialTheme.colorScheme.secondary)
        }
      )


      Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = tagInput,
          onValueChange = { tagInput = it },
          label = { Text("Adicionar tag") },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
          ),
          singleLine = true,
          trailingIcon = {
            IconButton(onClick = {
              if (tagInput.isNotEmpty() && !(tagList.contains(tagInput))) tagList.add(tagInput)
            }) {
              Icon(Icons.Default.Add, null)
            }
          },
          keyboardActions = KeyboardActions(
            onNext = {

              if (tagInput.isNotEmpty() && !(tagList.contains(tagInput))) tagList.add(tagInput)
            }
          )
        )

        LazyRow(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(tagList) { item ->
            InputChip(
              colors = InputChipDefaults.inputChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.2f)
              ),
              selected = true,
              onClick = {},
              trailingIcon = {
                IconButton(onClick = {
                  tagList.remove(item)
                }) { Icon(Icons.Default.Close, null) }
              },
              label = { Text(item) }
            )
          }
        }

        var showCancelDialog by remember { mutableStateOf(false) }
        if (showCancelDialog)
          AlertDialog(
            title = {
              Text("Confirmar cancelamento")
            },
            text = { Text("Deseja descartar as alterações?") },
            onDismissRequest = {},
            dismissButton = {
              Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
              showCancelDialog = false
                }) { Text("Continuar editando") }
            },
            confirmButton = {
              TextButton(onClick = {

                }) { Text("Descartar", color = MaterialTheme.colorScheme.error) }
            }

          )
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Button(
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              MaterialTheme.colorScheme.error
            ),
            onClick = {
              showCancelDialog = true
            }
          ) {
            Text("Cancelar" )
            Icon(Icons.Default.Close, null)

          }
          Button(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            onClick = {
              scope.launch {
                if (nome.isEmpty()) sh.showSnackbar(
                  "Nome é obrigatório",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (nome.length < 3) sh.showSnackbar(
                  "Nome deve ter ao menos 3 caracteres",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (selectedCategoria.isEmpty()) sh.showSnackbar(
                  "Selecione uma categoria",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (horario.isEmpty()) sh.showSnackbar(
                  "Selecione um horario alvo",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (selectedFrequencia.isEmpty()) sh.showSnackbar(
                  "Selecione a frequência",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (selectedFrequencia == "Personalizado" && selectedDiasSemana.isEmpty()) sh.showSnackbar(
                  "Selecione ao menos um dia",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
                else if (tagList.size > 4) sh.showSnackbar(
                  "Limite de 4 tags atingido",
                  duration = SnackbarDuration.Short,
                  withDismissAction = true
                )
              }
            }) {
            Text("Salvar")
          }
        }
      }
    }
  }
}
