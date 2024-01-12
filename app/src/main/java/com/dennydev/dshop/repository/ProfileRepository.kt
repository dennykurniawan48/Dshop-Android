package com.dennydev.dshop.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listorder.Order
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.logoutresponse.LogoutResponse
import com.dennydev.dshop.model.productresponse.Product
import com.dennydev.dshop.network.ApiProductImpl
import com.dennydev.dshop.network.ApiProfileImpl
import com.dennydev.dshop.pagingsource.OrderDataSource
import com.dennydev.dshop.pagingsource.ProductDataSource
import com.dennydev.dshop.profileresponse.ProfileResponse
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ProfileRepository @Inject constructor(val client: HttpClient) {

    suspend fun getProfile(token: String): ApiResponse<ProfileResponse>{
        return ApiProfileImpl(client).getProfile(token)
    }

    fun getProfileOrder(token: String): Flow<PagingData<Order>> {
        return Pager(PagingConfig(pageSize = 10)) {
            OrderDataSource(client, token)
        }.flow
    }

    suspend fun logout(token: String): ApiResponse<LogoutResponse>{
        return ApiProfileImpl(client).logout(token)
    }
}