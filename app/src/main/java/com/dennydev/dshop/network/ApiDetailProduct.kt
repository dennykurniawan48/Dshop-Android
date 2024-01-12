package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.detailproduct.DetailProduct
import com.dennydev.dshop.model.review.ReviewResponse

interface ApiDetailProduct {
    suspend fun getDetailProduct(id: String): ApiResponse<DetailProduct>
    suspend fun getIsProductInFavorite(token: String, id: String): ApiResponse<CheckIsInFavorite>
    suspend fun getReview(id: String): ApiResponse<ReviewResponse>
    suspend fun addFavorite(id: String, token: String)
    suspend fun deleteFavorite(id: String, token: String)
}