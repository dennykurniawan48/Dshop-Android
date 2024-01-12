package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.logoutresponse.LogoutResponse
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.profileresponse.ProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class ApiProfileImpl(val client: HttpClient) : ApiProfile {
    override suspend fun getProfile(token: String): ApiResponse<ProfileResponse> {
        try {
            val response: ProfileResponse = client.get(Constant.PROFILE_URL) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            return ApiResponse.Success(response)
        } catch (e: Exception) {
            Log.d("error profile", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getProfileOrder(page: Int, limit: Int, token: String): ListOrder {
        val response: HttpResponse = client.get(
            Constant.PROFILE_ORDER_URl.replace("{page}", page.toString())
                .replace("{limit}", "5")
        ){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return response.body()
    }

    override suspend fun logout(token: String): ApiResponse<LogoutResponse> {
        return try {
            val response: HttpResponse = client.get(
                Constant.LOGOUT_URL
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            ApiResponse.Error("something went wrong")
        }
    }
}