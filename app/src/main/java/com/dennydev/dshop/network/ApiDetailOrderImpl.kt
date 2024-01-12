package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.detailorder.DetailOrder
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class ApiDetailOrderImpl(val client: HttpClient): ApiDetailOrder {
    override suspend fun getDeatailOrder(id: String): ApiResponse<DetailOrder> {
        try {
            val response: DetailOrder = client.get(
                Constant.ORDER_URL+"/$id"
            ).body()
            return ApiResponse.Success(response)
        }catch(e: Exception){
            Log.d("error detail order", e.toString())
            return ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getPaymentSheetData(total: Double, token: String, orderId: String): ApiResponse<PaymentSheetData>{
        return try{
            val response: HttpResponse = client.get(Constant.PAYMENT_SHEET_URL.replace("{total}", total.toString()).replace("{orderId}", orderId)){
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error get payment sheet", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }
}