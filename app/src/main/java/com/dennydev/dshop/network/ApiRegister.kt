package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.registerresponse.RegisterResponse

interface ApiRegister {
    suspend fun register(firstName: String, lastName: String, email: String, password: String, confirmPass: String): ApiResponse<RegisterResponse>
}