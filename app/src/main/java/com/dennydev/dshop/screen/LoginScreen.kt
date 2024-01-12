package com.dennydev.dshop.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dennydev.dshop.R
import com.dennydev.dshop.component.GoogleSignInHelper
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.LoginViewModel
import com.dennydev.dshop.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val errorEmail by loginViewModel.errorEmailLogin
    val errorPassword by loginViewModel.errorPasswordLogin
    val email by loginViewModel.emailLogin
    val password by loginViewModel.passwordLogin
    val showPassword by loginViewModel.showPassword
    val googleSelected by loginViewModel.isGoogleSignIn
    val loginResponse by loginViewModel.loginResponse
    val fcmToken by loginViewModel.fcmToken
    val signInStatus by loginViewModel.isSignedIn

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit){
        loginViewModel.getFcmToken()
    }

    LaunchedEffect(key1 = signInStatus){
        if(signInStatus){
            navController.navigate(Screen.HomeScreen.route){
                popUpTo(Screen.HomeScreen.route){
                    inclusive = true
                }
            }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Login") })
    }) {it ->
        GoogleSignInHelper(context = context, onGoogleSignInSeleced = googleSelected, onDismiss = {loginViewModel.onChangeIsGoogleLogin(false)}, onResultReceived = {
            token -> loginViewModel.loginGoogle(token, fcmToken.toString())
        })
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(it)
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Email", style = MaterialTheme.typography.labelSmall)
                Text(errorEmail, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = {
                    loginViewModel.onChangeEmailLogin(it)
            }, placeholder = {Text("Enter email")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Enter Email")
            }, modifier=Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Password", style = MaterialTheme.typography.labelSmall)
                Text(errorPassword, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = {
                    loginViewModel.onChangePasswordLogin(it)
            }, placeholder = {Text("Enter password")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Enter Password")
            }, modifier=Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = {
                    loginViewModel.onChangeShowPassword()
                }) {
                    Icon(imageVector = if(showPassword)  Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                }
            }, visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if(loginResponse is ApiResponse.Error){
                Spacer(modifier = Modifier.height(24.dp))
                Text(loginResponse.message.toString(), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(loginResponse !is ApiResponse.Loading) {
                    loginViewModel.loginCredentials()
                }
            }, modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(if(loginResponse is ApiResponse.Loading) "Loading..." else "Login", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Or")
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = {
                if(loginResponse !is ApiResponse.Loading) {
                    loginViewModel.onChangeIsGoogleLogin(true)
                }
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier=Modifier.width(8.dp))
                    Text(if(loginResponse is ApiResponse.Loading) "Loading..." else "Login with Google")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = {
                navController.navigate(Screen.RegisterScreen.route)
            }) {
                Text("Dont have an account? Register")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun PrevLogin() {
    Scaffold {it ->
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(it)
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Login", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Email")
                Text("Invalid email.", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = {Text("Enter email")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Enter Email")
            }, modifier=Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Password")
                Text("Invalid password.", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = {Text("Enter email")}, leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Enter Password")
            }, modifier=Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(36.dp))
            Button(onClick = { /*TODO*/ }, modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text("Login", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Or")
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = {

            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier=Modifier.width(8.dp))
                    Text("Login with Google")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = {

            }) {
                Text("Dont have an account? Register")
            }
        }
    }
}