package com.dennydev.dshop.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dennydev.dshop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoScreen(navController: NavHostController) {
    Scaffold(modifier= Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = {  }, actions = {
            IconButton(onClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("usePromo", false)
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        })
    }){ values ->
        Column(modifier= Modifier
            .fillMaxSize()
            .padding(values)
            .padding(horizontal = 24.dp)) {
            Spacer(modifier=Modifier.height(24.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.discount),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier=Modifier.height(48.dp))
            Row {
                Text("Get 50% discount", style = MaterialTheme.typography.headlineSmall, color=MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier=Modifier.height(16.dp))
            Text("Save up 50% for your order with:", style=MaterialTheme.typography.titleLarge)
            Spacer(modifier=Modifier.height(48.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("PORTFOLIO50", style = MaterialTheme.typography.headlineSmall, color=MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier=Modifier.height(48.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                OutlinedButton(onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("usePromo", false)
                    navController.popBackStack()
                }) {
                    Text("No, thanks")
                }
                Button(onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("usePromo", true)
                    navController.popBackStack()
                }) {
                    Text("Use Promocode")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Prevpromo() {
    Scaffold(modifier= Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = "Promo") })
    }, bottomBar = {
        Column(modifier = Modifier.fillMaxWidth()) {

        }
    }){ values ->
        Column(modifier= Modifier
            .fillMaxSize()
            .padding(values)
            .padding(horizontal = 24.dp)) {
            Spacer(modifier=Modifier.height(24.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.discount),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier=Modifier.height(48.dp))
            Row {
                Text("Get 50% discount", style = MaterialTheme.typography.headlineSmall, color=MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier=Modifier.height(16.dp))
            Text("Save up 50% for your order with:", style=MaterialTheme.typography.titleLarge)
            Spacer(modifier=Modifier.height(24.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("PORTFOLIO50", style = MaterialTheme.typography.headlineSmall, color=MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier=Modifier.height(48.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text("No, thanks")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text("Use Promocode")
                }
            }
        }
    }
}