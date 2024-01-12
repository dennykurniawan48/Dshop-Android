package com.dennydev.dshop.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorSearch() {
    Column(modifier= Modifier
        .fillMaxWidth()
        .padding(top = 6.dp, bottom = 6.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Error Load data...", style = MaterialTheme.typography.bodyMedium, color=MaterialTheme.colorScheme.error)
    }
}