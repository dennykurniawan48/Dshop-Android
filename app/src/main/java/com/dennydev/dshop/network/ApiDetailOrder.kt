package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.detailorder.DetailOrder
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData

interface ApiDetailOrder {
    suspend fun getDeatailOrder(id: String): ApiResponse<DetailOrder>
    suspend fun getPaymentSheetData(total: Double, token: String, orderId: String): ApiResponse<PaymentSheetData>
}