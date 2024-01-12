package com.dennydev.dshop.repository

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.registerresponse.RegisterResponse
import com.dennydev.dshop.network.ApiRegisterImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class RegisterRepository  @Inject constructor(val client: HttpClient) {
    suspend fun registerCredentials(firstName: String,
                                    lastName: String,
                                    email: String,
                                    password: String,
                                    confirmPass: String
    ): ApiResponse<RegisterResponse>{
        return ApiRegisterImpl(client).register(firstName, lastName, email, password, confirmPass)
    }
}