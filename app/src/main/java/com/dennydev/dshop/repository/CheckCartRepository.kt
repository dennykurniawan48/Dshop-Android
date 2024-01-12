package com.dennydev.dshop.repository

import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.cartresponse.CartResponse
import com.dennydev.dshop.model.checkoutresponse.CheckoutResponse
import com.dennydev.dshop.model.countrystate.CountryState
import com.dennydev.dshop.model.delivery.Delivery
import com.dennydev.dshop.model.form.checkout.CheckoutForm
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import com.dennydev.dshop.network.ApiCheckCartImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class CheckCartRepository @Inject constructor(val client: HttpClient) {
    suspend fun getCartData(data: List<OrderCart>): ApiResponse<CartResponse>{
        return ApiCheckCartImpl(client).getCartData(data)
    }
    suspend fun getDelivery(): ApiResponse<Delivery>{
        return ApiCheckCartImpl(client).getDelivery()
    }

    suspend fun getCountryState(): ApiResponse<CountryState>{
        return ApiCheckCartImpl(client).getCountryState()
    }
    suspend fun checkout(data: CheckoutForm, token: String): ApiResponse<CheckoutResponse>{
        return ApiCheckCartImpl(client).checkout(data, token)
    }
}