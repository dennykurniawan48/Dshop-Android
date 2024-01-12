package com.dennydev.dshop.repository

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.network.ApiLoginImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class LoginRepository @Inject constructor(val client: HttpClient) {
    suspend fun loginWithGoogle(googleToken: String, fcmToken: String): ApiResponse<LoginResponse> {
        return ApiLoginImpl(client).postLoginGoogle(googleToken, fcmToken)
    }
    suspend fun loginWithCredentials(email: String, password: String, fcmToken: String): ApiResponse<LoginResponse>{
        return ApiLoginImpl(client).postLoginCredentials(email, password, fcmToken)
    }
}