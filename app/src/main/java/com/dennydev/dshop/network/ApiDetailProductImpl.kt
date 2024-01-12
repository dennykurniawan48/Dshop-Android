package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.detailproduct.DetailProduct
import com.dennydev.dshop.model.review.ReviewResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.json.JSONObject

class ApiDetailProductImpl(val client: HttpClient):
    ApiDetailProduct {
    override suspend fun getDetailProduct(id: String): ApiResponse<DetailProduct> {
        try {
            val response: HttpResponse = client.get(
                Constant.DETAIL_PRODUCT_URL.replace("{id}", id)
            )
            return ApiResponse.Success(response.body<DetailProduct>())
        }catch(e: Exception){
            Log.d("error detail product", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getIsProductInFavorite( token: String, id: String): ApiResponse<CheckIsInFavorite> {
        try {
            val response: CheckIsInFavorite = client.get(
                Constant.CHECK_PRODUCT_IN_FAVORITE_URL.replace("{id}", id)
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body()
            return ApiResponse.Success(response)
        }catch(e: Exception){
            Log.d("error check in favorite", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getReview(id: String): ApiResponse<ReviewResponse> {
        try {
            val response: ReviewResponse = client.get(
                Constant.PRODUCT_REVIEW_URL.replace("{id}", id)
            ).body()
            return ApiResponse.Success(response)
        }catch(e: Exception){
            Log.d("error review", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun addFavorite(id: String, token: String) {
        val product = JSONObject()
        product.put("productId", id)
        try {
            client.post(
                Constant.FAVORITE_URL
            ){
                contentType(ContentType.Application.Json)
                setBody(TextContent(product.toString(), contentType = ContentType.Application.Json))
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }catch(e: Exception){
            Log.d("error add favorite", e.toString())
        }
    }

    override suspend fun deleteFavorite(id: String, token: String) {
        try {
            val response: HttpResponse = client.delete(
                Constant.FAVORITE_URL+"?id=${id}"
            ){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            Log.d("response", response.toString())
        }catch(e: Exception){
            Log.d("error remove favorite", e.toString())
        }
    }
}