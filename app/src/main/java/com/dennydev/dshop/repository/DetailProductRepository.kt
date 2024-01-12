package com.dennydev.dshop.repository

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.detailproduct.DetailProduct
import com.dennydev.dshop.model.review.ReviewResponse
import com.dennydev.dshop.network.ApiDetailProductImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class DetailProductRepository @Inject constructor(val client: HttpClient) {
    suspend fun getIsProductInFavorite(token: String, id: String): ApiResponse<CheckIsInFavorite>{
        return ApiDetailProductImpl(client).getIsProductInFavorite(token, id)
    }

    suspend fun getDetailProduct(id: String): ApiResponse<DetailProduct>{
        return ApiDetailProductImpl(client).getDetailProduct(id)
    }

    suspend fun getReviewProduct(id: String): ApiResponse<ReviewResponse>{
        return ApiDetailProductImpl(client).getReview(id)
    }

    suspend fun addToFavorite(id: String, token: String){
        ApiDetailProductImpl(client).addFavorite(id, token)
    }

    suspend fun deleteFromFavorite(id: String, token: String){
        ApiDetailProductImpl(client).deleteFavorite(id, token)
    }
}