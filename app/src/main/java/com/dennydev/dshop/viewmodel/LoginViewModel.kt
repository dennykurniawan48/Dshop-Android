package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.LoginRepository
import com.dennydev.dshop.repository.MainRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: LoginRepository
): ViewModel() {
    private val _errorEmailLogin: MutableState<String> = mutableStateOf("")
    private val _errorPasswordLogin: MutableState<String> = mutableStateOf("")
    val errorEmailLogin: State<String> = _errorEmailLogin
    val errorPasswordLogin: State<String> = _errorPasswordLogin

    private val _fcmToken: MutableState<String?> = mutableStateOf(null)
    val fcmToken: State<String?> = _fcmToken

    fun getFcmToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            _fcmToken.value = task.result
        })
    }

    private val _emailLogin: MutableState<String> = mutableStateOf("")
    private val _passwordLogin: MutableState<String> = mutableStateOf("")
    val emailLogin: State<String> = _emailLogin
    val passwordLogin: State<String> = _passwordLogin

    private val _showPassword: MutableState<Boolean> = mutableStateOf(false)
    val showPassword: State<Boolean> = _showPassword

    private val _isGoogleSignIn: MutableState<Boolean> = mutableStateOf(false)
    val isGoogleSignIn: State<Boolean> = _isGoogleSignIn

    private val _isSignedIn: MutableState<Boolean> = mutableStateOf(false)
    val isSignedIn: State<Boolean> = _isSignedIn

    private val _loginResponse: MutableState<ApiResponse<LoginResponse>> = mutableStateOf(ApiResponse.Idle())
    val loginResponse: State<ApiResponse<LoginResponse>> = _loginResponse

    init {
        viewModelScope.launch {
            datastoreRepository.flowToken.collect { token ->
                _isSignedIn.value = token.isNotEmpty()
            }
        }
    }

    fun onChangeIsGoogleLogin(status: Boolean){
        _isGoogleSignIn.value = status
    }

    fun onChangeShowPassword(){
        _showPassword.value = !showPassword.value
    }

    fun onChangeEmailLogin(it: String){
        _emailLogin.value = it
        checkEmailValid()
    }

    fun onChangePasswordLogin(it: String){
        _passwordLogin.value = it
        checkPasswordValid()
    }

    private fun checkEmailValid(){
        _errorEmailLogin.value = ""
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        if(!emailRegex.matches(_emailLogin.value)) {
            _errorEmailLogin.value = "Email isn't valid."
        }
        if(_emailLogin.value.isEmpty()){
            _errorEmailLogin.value = "Can't empty."
        }
    }

    private fun checkPasswordValid(){
        _errorPasswordLogin.value = ""
        if(_passwordLogin.value.isEmpty()){
            _errorPasswordLogin.value = "Can't empty."
        }else if(_passwordLogin.value.length < 5){
            _errorPasswordLogin.value = "Min 5 character."
        }
    }

    fun loginCredentials(){
        checkEmailValid()
        checkPasswordValid()
        if(_errorEmailLogin.value.isEmpty() && _errorPasswordLogin.value.isEmpty()) {
            _loginResponse.value = ApiResponse.Loading()
            viewModelScope.launch {
                val response = repository.loginWithCredentials(emailLogin.value, passwordLogin.value, fcmToken.value.toString())
                if(response is ApiResponse.Success){
                    _loginResponse.value = response
                    response.data?.data?.let {
                        datastoreRepository.saveToken(token = it.accessToken, exp = it.expiresIn, google = it.google)
                        _isSignedIn.value = true
                    }
                }
                _loginResponse.value = response
            }
        }
    }

    fun loginGoogle(token: String, fcmToken: String){
        _loginResponse.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = repository.loginWithGoogle(token, fcmToken)
            if(response is ApiResponse.Success){
                _loginResponse.value = response
                response.data?.data?.let {
                    datastoreRepository.saveToken(token = it.accessToken, exp = it.expiresIn, google = it.google)
                    _isSignedIn.value = true
                }
            }
            _loginResponse.value = response
        }
    }
}