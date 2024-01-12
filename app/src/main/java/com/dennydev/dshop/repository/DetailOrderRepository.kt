package com.dennydev.dshop.repository

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.detailorder.DetailOrder
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import com.dennydev.dshop.network.ApiCheckCartImpl
import com.dennydev.dshop.network.ApiDetailOrderImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class DetailOrderRepository @Inject constructor(val client: HttpClient) {
    suspend fun getDetailOrder(id: String): ApiResponse<DetailOrder>{
        return ApiDetailOrderImpl(client).getDeatailOrder(id)
    }
    suspend fun getPaymentSheetData(total: Double, token: String, orderId: String): ApiResponse<PaymentSheetData>{
        return ApiDetailOrderImpl(client).getPaymentSheetData(total, token, orderId)
    }
}