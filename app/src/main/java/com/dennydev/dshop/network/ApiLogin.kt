package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.loginresponse.LoginResponse

interface ApiLogin {
    suspend fun postLoginGoogle(token: String, fcmToken: String): ApiResponse<LoginResponse>
    suspend fun postLoginCredentials(email: String, password: String, fcmToken: String): ApiResponse<LoginResponse>
}