package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.loginresponse.LoginResponse
import com.dennydev.dshop.model.logoutresponse.LogoutResponse
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.profileresponse.ProfileResponse

interface ApiProfile {
    suspend fun getProfile(token: String): ApiResponse<ProfileResponse>
    suspend fun getProfileOrder(page: Int, limit: Int, token: String): ListOrder
    suspend fun logout(token: String): ApiResponse<LogoutResponse>
}