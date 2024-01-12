package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.listfavorite.ListFavorite
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

class ApiFavoriteImpl(val client: HttpClient): ApiFavorite {
    override suspend fun getFavorite(token: String): ApiResponse<ListFavorite> {
        try {
            val response: ListFavorite = client.get(
                Constant.FAVORITE_URL
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            return ApiResponse.Success(response)
        }catch(e: Exception){
            Log.d("error get favorite", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }
}