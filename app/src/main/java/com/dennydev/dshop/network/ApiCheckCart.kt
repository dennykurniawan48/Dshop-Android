package com.dennydev.dshop.network

import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.cartresponse.CartResponse
import com.dennydev.dshop.model.checkoutresponse.CheckoutResponse
import com.dennydev.dshop.model.countrystate.CountryState
import com.dennydev.dshop.model.delivery.Delivery
import com.dennydev.dshop.model.form.checkout.CheckoutForm
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData

interface ApiCheckCart {
    suspend fun getCartData(data: List<OrderCart>): ApiResponse<CartResponse>
    suspend fun getDelivery(): ApiResponse<Delivery>
    suspend fun getCountryState(): ApiResponse<CountryState>
    suspend fun checkout(data: CheckoutForm, token: String): ApiResponse<CheckoutResponse>
}