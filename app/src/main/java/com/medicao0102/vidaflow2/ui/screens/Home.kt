package com.medicao0102.vidaflow2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home() {
  Column(Modifier
    .fillMaxSize()
    .background(MaterialTheme.colorScheme.background))
  {
    Text("home")
  }
}