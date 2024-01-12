package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.listcategory.ListCategory
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.sliderresponse.SliderResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ApiHomeImpl(val client: HttpClient): ApiHome {
    override suspend fun getSlider(): ApiResponse<SliderResponse> {
        try {
            val response: HttpResponse = client.get(
                Constant.SLIDER_URL
            )
            return ApiResponse.Success(response.body<SliderResponse>())
        }catch(e: Exception){
            Log.d("error slider", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getNewestProduct(): ApiResponse<ProductResponse> {
        try {
            val response: HttpResponse = client.get(
                Constant.PRODUCT_URL.replace("{page}", "1")
                    .replace("{limit}", "12").replace("{query}", "")
            )
            return ApiResponse.Success(response.body<ProductResponse>())
        }catch(e: Exception){
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getCategory(): ApiResponse<ListCategory> {
        try {
            val response: HttpResponse = client.get(
                Constant.CATEGORY_URL
            )
            return ApiResponse.Success(response.body<ListCategory>())
        }catch(e: Exception){
            return ApiResponse.Error("Something wen't wrong")
        }
    }
}