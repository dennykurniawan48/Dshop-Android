package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.registerresponse.RegisterResponse
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.LoginRepository
import com.dennydev.dshop.repository.MainRepository
import com.dennydev.dshop.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: RegisterRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _errorEmailRegister: MutableState<String> = mutableStateOf("")
    private val _errorFirstnameRegister: MutableState<String> = mutableStateOf("")
    private val _errorLastnameRegister: MutableState<String> = mutableStateOf("")
    private val _errorPasswordRegister: MutableState<String> = mutableStateOf("")
    private val _errorConfirmPasswordRegister: MutableState<String> = mutableStateOf("")
    val errorEmailRegister: State<String> = _errorEmailRegister
    val errorFirstnameRegister: State<String> = _errorFirstnameRegister
    val errorLastnameRegister: State<String> = _errorLastnameRegister
    val errorPasswordRegister: State<String> = _errorPasswordRegister
    val errorConfirmPasswordRegister: State<String> = _errorConfirmPasswordRegister

    private val _emailRegister: MutableState<String> = mutableStateOf("")
    private val _firstnameRegister: MutableState<String> = mutableStateOf("")
    private val _lastnameRegister: MutableState<String> = mutableStateOf("")
    private val _passwordRegister: MutableState<String> = mutableStateOf("")
    private val _confirmPasswordRegister: MutableState<String> = mutableStateOf("")
    val emailRegister: State<String> = _emailRegister
    val firstnameRegister: State<String> = _firstnameRegister
    val lastnameRegister: State<String> = _lastnameRegister
    val passwordRegister: State<String> = _passwordRegister
    val confirmPasswordRegister: State<String> = _confirmPasswordRegister

    private val _registerResponse: MutableState<ApiResponse<RegisterResponse>> = mutableStateOf(ApiResponse.Idle())
    val registerResponse: State<ApiResponse<RegisterResponse>> = _registerResponse

    private val _showPassword: MutableState<Boolean> = mutableStateOf(false)
    private val _showConfirm: MutableState<Boolean> = mutableStateOf(false)

    val showPassword: State<Boolean> = _showPassword
    val showConfirm: State<Boolean> = _showConfirm

    fun onChangeShowPassword(){
        _showPassword.value = !showPassword.value
    }

    fun onChangeShowConfirm(){
        _showConfirm.value = !showConfirm.value
    }

    fun onChangeEmail(it: String){
        _emailRegister.value = it
        checkEmail()
    }

    fun onChangeFirstname(it: String){
        _firstnameRegister.value = it
        checkFirstName()
    }

    fun onChangeLastname(it: String){
        _lastnameRegister.value = it
        checkLastName()
    }

    fun onChangePassword(it: String){
        _passwordRegister.value = it
        checkPassword()
    }

    fun onChangeConfirmPassword(it: String){
        _confirmPasswordRegister.value = it
        checkConfirmPassword()
    }

    private fun checkEmail(){
        _errorEmailRegister.value = ""
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        if(!emailRegex.matches(_emailRegister.value)) {
            _errorEmailRegister.value = "Email isn't valid."
        }
        if(_emailRegister.value.isEmpty()){
            _errorEmailRegister.value = "Can't empty."
        }
    }

    private fun checkFirstName(){
        _errorFirstnameRegister.value = ""
        if(_firstnameRegister.value.isEmpty()) {
            _errorFirstnameRegister.value = "Can't empty."
        }else if(_firstnameRegister.value.length < 4){
            _errorFirstnameRegister.value = "Min 4 character."
        }
    }

    private fun checkLastName(){
        _errorLastnameRegister.value = ""
        if(_lastnameRegister.value.isEmpty()) {
            _errorLastnameRegister.value = "Can't empty."
        }else if(_lastnameRegister.value.length < 4){
            _errorLastnameRegister.value = "Min 4 character."
        }
    }

    private fun checkPassword(){
        _errorPasswordRegister.value = ""
        if(_passwordRegister.value.isEmpty()){
            _errorPasswordRegister.value = "Can't empty."
        } else if(_passwordRegister.value.length < 5){
            _errorPasswordRegister.value = "Min 5 character."
        }
    }

    private fun checkConfirmPassword(){
        _errorConfirmPasswordRegister.value = ""
        if(_confirmPasswordRegister.value.isEmpty()){
            _errorConfirmPasswordRegister.value = "Can't empty"
        }else if(_confirmPasswordRegister.value != _passwordRegister.value){
            _errorConfirmPasswordRegister.value = "Password not match."
        }
    }

    fun register(){
        checkAll()
        if(_errorEmailRegister.value.isEmpty() && _errorFirstnameRegister.value.isEmpty() && _errorLastnameRegister.value.isEmpty() && _errorPasswordRegister.value.isEmpty() && _errorConfirmPasswordRegister.value.isEmpty()){
            _registerResponse.value = ApiResponse.Loading()
            viewModelScope.launch {
                _registerResponse.value = repository.registerCredentials(
                    _firstnameRegister.value, _lastnameRegister.value, _emailRegister.value, _passwordRegister.value, _confirmPasswordRegister.value
                )
            }
        }
    }

    private fun checkAll(){
        checkEmail()
        checkFirstName()
        checkLastName()
        checkPassword()
        checkConfirmPassword()
    }
}