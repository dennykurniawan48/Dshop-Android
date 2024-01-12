package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.CredentialsParams
import com.dennydev.dshop.model.loginresponse.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable


class ApiLoginImpl(val client: HttpClient) : ApiLogin{
    override suspend fun postLoginGoogle(token: String, fcmToken: String): ApiResponse<LoginResponse> {
        return try {
            val response: HttpResponse = client.get(
                Constant.LOGIN_GOOGLE_URL
                    .replace("{token}", token)
                    .replace("{fcmtoken}", fcmToken)
            )
            if (response.status.isSuccess()) {
                val myResponse = response.body<LoginResponse>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 401) {
                ApiResponse.Error("Wrong email or password.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch(e: Exception){
            ApiResponse.Error("Something went wrong.")
        }
    }

    override suspend fun postLoginCredentials(
        email: String,
        password: String,
        fcmToken: String
    ): ApiResponse<LoginResponse> {
        return try {
            val response: HttpResponse = client.post(Constant.LOGIN_CREDENTIALS_URL) {
                contentType(ContentType.Application.Json)
                setBody(CredentialsParams(email, password, fcmToken))
            }
            if (response.status.isSuccess()) {
                val myResponse = response.body<LoginResponse>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 401) {
                ApiResponse.Error("Wrong username or password.")
            } else if (response.status.value == 404) {
                ApiResponse.Error("Email isn't registered.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch(e: Exception){
            ApiResponse.Error("Something went wrong.")
        }
    }
}