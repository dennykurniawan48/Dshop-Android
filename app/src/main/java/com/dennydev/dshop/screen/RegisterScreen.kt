package com.dennydev.dshop.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dennydev.dshop.R
import com.dennydev.dshop.component.GoogleSignInHelper
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.LoginViewModel
import com.dennydev.dshop.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel, loginViewModel: LoginViewModel) {
    val errorEmail by registerViewModel.errorEmailRegister
    val errorFirstname by registerViewModel.errorFirstnameRegister
    val errorLastname by registerViewModel.errorLastnameRegister
    val errorPassword by registerViewModel.errorPasswordRegister
    val errorConfirm by registerViewModel.errorConfirmPasswordRegister

    val email by registerViewModel.emailRegister
    val firstname by registerViewModel.firstnameRegister
    val lastName by registerViewModel.lastnameRegister
    val password by registerViewModel.passwordRegister
    val confirm by registerViewModel.confirmPasswordRegister

    val showPassword by registerViewModel.showPassword
    val showConfirm by registerViewModel.showConfirm

    val googleSelected by loginViewModel.isGoogleSignIn

    val registerResponse by registerViewModel.registerResponse
    val loginResponse by loginViewModel.loginResponse
    val isSignedIn by loginViewModel.isSignedIn
    val fcmToken by loginViewModel.fcmToken

    val context = LocalContext.current

    LaunchedEffect(key1 = registerResponse){
        if(registerResponse is ApiResponse.Success){
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = Unit){
        loginViewModel.getFcmToken()
    }

    LaunchedEffect(key1 = isSignedIn){
        if(isSignedIn){
            navController.navigate(Screen.HomeScreen.route){
                popUpTo(Screen.HomeScreen.route){
                    inclusive=true
                }
            }
        }
    }

    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Register") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back")
            }
        })
    }) {it ->
        GoogleSignInHelper(context = context, onGoogleSignInSeleced = googleSelected, onDismiss = {loginViewModel.onChangeIsGoogleLogin(false)}, onResultReceived = {
                token -> loginViewModel.loginGoogle(token, fcmToken.toString())
        })
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(it)
            .verticalScroll(scrollState)
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("First name", style = MaterialTheme.typography.labelSmall)
                Text(errorFirstname, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = firstname, onValueChange = {
                     registerViewModel.onChangeFirstname(it)
            }, placeholder = { Text("Enter your first name") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Enter first name")
            }, modifier= Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Last name", style = MaterialTheme.typography.labelSmall)
                Text(errorLastname, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = lastName, onValueChange = {
                registerViewModel.onChangeLastname(it)
            }, placeholder = { Text("Enter your last name") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Enter last name")
            }, modifier= Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Email", style = MaterialTheme.typography.labelSmall)
                Text(errorEmail, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = {
                  registerViewModel.onChangeEmail(it)
            }, placeholder = { Text("Enter email") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Enter Email")
            }, modifier= Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Password", style = MaterialTheme.typography.labelSmall)
                Text(errorPassword, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = {
                 registerViewModel.onChangePassword(it)
            }, placeholder = { Text("Enter password") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Enter Password")
            }, modifier= Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = { registerViewModel.onChangeShowPassword() }) {
                    Icon(imageVector = if(showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                }
            }, visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Confirm pass", style = MaterialTheme.typography.labelSmall)
                Text(errorConfirm, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = confirm, onValueChange = {
                registerViewModel.onChangeConfirmPassword(it)
            }, placeholder = { Text("Enter password") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Password, contentDescription = "Confirm password")
            }, modifier= Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = { registerViewModel.onChangeShowConfirm() }) {
                    Icon(imageVector = if(showConfirm) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "")
                }
            }, visualTransformation = if(showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if(!(registerResponse is ApiResponse.Loading || loginResponse is ApiResponse.Loading)){
                    registerViewModel.register()
                }
            }, modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(if(registerResponse is ApiResponse.Loading || loginResponse is ApiResponse.Loading)  "Loading" else "Register", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text("Or")
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = {
                if(!(registerResponse is ApiResponse.Loading || loginResponse is ApiResponse.Loading)) {
                    loginViewModel.onChangeIsGoogleLogin(true)
                }
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier= Modifier.width(8.dp))
                    Text(if(registerResponse is ApiResponse.Loading || loginResponse is ApiResponse.Loading) "Loading" else "Register with Google")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? Login")
            }
        }
    }
}