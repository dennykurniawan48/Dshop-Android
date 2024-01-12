package com.dennydev.dshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.navigation.SetupNavigation
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.screen.LoginScreen
import com.dennydev.dshop.ui.theme.DshopTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authStoreRepository = AuthStoreRepository(this)
//        var showSplashScren = true
        installSplashScreen()
//            .setKeepOnScreenCondition{
//            showSplashScren
//        }

        setContent {
            val navController = rememberNavController()
            val token by authStoreRepository.flowToken.collectAsState(initial = "")
//            LaunchedEffect(key1 = true){
//                delay(1500)
//                showSplashScren = false
//            }
            DshopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = if(token.isEmpty()) Screen.LoginScreen.route else Screen.HomeScreen.route
                    SetupNavigation(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}
