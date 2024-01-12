package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listfavorite.ListFavorite

interface ApiFavorite {
    suspend fun getFavorite(token: String): ApiResponse<ListFavorite>
}