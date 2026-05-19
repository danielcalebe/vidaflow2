package com.medicao0102.vidaflow2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.exp
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarHabito(modifier: Modifier = Modifier) {

  var nome by remember { mutableStateOf("") }
  var descricao by remember { mutableStateOf("") }
  var categoria by remember { mutableStateOf("") }

  Column(
    Modifier
      .fillMaxSize()
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

      }) {
        Icon(Icons.Default.ArrowBack, null)
      }
      Text(
        "Novo Hábito",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
      )


    }


    Column(
      Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

      Column(Modifier.fillMaxWidth()) {
        Text(
          "Adicione um novo hábito à sua lista",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp)
        )
      }
      OutlinedTextField(
        value = nome,
        onValueChange = { nome = it },
        label = { Text("Nome do hábito") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = descricao,
        onValueChange = { descricao = it },
        label = { Text("Descrição") },
        minLines = 3,
        singleLine = false,
        modifier = Modifier.fillMaxWidth()

      )

      var categoriaExpanded by remember { mutableStateOf(false) }
      var selectedCategoria by remember { mutableStateOf(CATEGORIAS.Outro) }

      ExposedDropdownMenuBox(
        expanded = categoriaExpanded,
        onExpandedChange = {},
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = selectedCategoria.name,
          onValueChange = {},
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

        DropdownMenu(
          expanded = categoriaExpanded,
          onDismissRequest = { categoriaExpanded = false },
          modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
          CATEGORIAS.values().forEach {
            DropdownMenuItem(
              text = { Text(it.name) },
              onClick = { selectedCategoria = it }
            )
          }
        }
      }
    }
  }
}