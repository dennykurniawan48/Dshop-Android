package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.CredentialsParams
import com.dennydev.dshop.model.RegisterParams
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.registerresponse.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ApiRegisterImpl(val client: HttpClient): ApiRegister {
    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPass: String
    ): ApiResponse<RegisterResponse> {
        return try {
            val response: HttpResponse = client.post(Constant.REGISTER_CREDENTIALS_URL) {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterParams(
                        email = email,
                        firstname = firstName,
                        lastname = lastName,
                        password = password,
                        confirmpass = confirmPass
                    )
                )
            }

            if (response.status.isSuccess()) {
                val myResponse = response.body<RegisterResponse>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 409) {
                ApiResponse.Error("Email already registered.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch (e: Exception){
            Log.d("Error register", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }
}