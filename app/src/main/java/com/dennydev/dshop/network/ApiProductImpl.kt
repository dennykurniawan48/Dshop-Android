package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.CredentialsParams
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.registerresponse.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ApiProductImpl(val client: HttpClient): ApiProduct {
    override suspend fun getProduct(page: Int, limit: Int, query: String): ProductResponse {
            val response: HttpResponse = client.get(Constant.PRODUCT_URL.replace("{page}", "10").replace("{limit}", limit.toString()).replace("{query}", query))
            return response.body()
    }
}