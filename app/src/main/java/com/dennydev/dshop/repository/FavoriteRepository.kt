package com.dennydev.dshop.repository

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listfavorite.ListFavorite
import com.dennydev.dshop.network.ApiFavoriteImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class FavoriteRepository @Inject constructor(val client: HttpClient) {
    suspend fun getListFavorite(token: String): ApiResponse<ListFavorite>{
        return ApiFavoriteImpl(client).getFavorite(token)
    }
}