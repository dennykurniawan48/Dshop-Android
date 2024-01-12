package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.CredentialsParams
import com.dennydev.dshop.model.cartresponse.CartResponse
import com.dennydev.dshop.model.checkoutresponse.CheckoutResponse
import com.dennydev.dshop.model.countrystate.CountryState
import com.dennydev.dshop.model.delivery.Delivery
import com.dennydev.dshop.model.form.checkout.CheckoutForm
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.json.JSONArray
import org.json.JSONObject

class ApiCheckCartImpl(val client: HttpClient): ApiCheckCart {
    override suspend fun getCartData(data: List<OrderCart>): ApiResponse<CartResponse> {
        val jsonArray = JSONArray()
        data.forEach {
            val jsonObject = JSONObject()
            jsonObject.put("id", it.productId)
            jsonObject.put("qty", it.qty)
            jsonArray.put(jsonObject)
        }

        return try {
            val response: HttpResponse = client.post(Constant.CHECKCART_URL) {
                contentType(ContentType.Application.Json)
                setBody(TextContent(jsonArray.toString(), contentType = ContentType.Application.Json))
            }
            val myResponse = response.body<CartResponse>()
            ApiResponse.Success(myResponse)
        }catch(e: Exception){
            Log.d("Error get cart", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }

    override suspend fun getDelivery(): ApiResponse<Delivery> {
        return try{
            val response: HttpResponse = client.get(Constant.SHIPPING_URL)
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error get delivery", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getCountryState(): ApiResponse<CountryState> {
        return try{
            val response: HttpResponse = client.get(Constant.COUNTRY_URL)
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error get payment sheet", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun checkout(data: CheckoutForm, token: String): ApiResponse<CheckoutResponse> {
        val response: HttpResponse = client.post(Constant.ORDER_URL) {
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(data)
        }
        return try{
            val myResponse = response.body<CheckoutResponse>()
            ApiResponse.Success(myResponse)
        }catch (e: Exception){
            Log.d("error checkout",e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }
}