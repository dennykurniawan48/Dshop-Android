package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.productresponse.ProductResponse

interface ApiProduct {
    suspend fun getProduct(page: Int, limit: Int, query: String): ProductResponse
}