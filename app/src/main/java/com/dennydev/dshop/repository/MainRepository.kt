package com.dennydev.dshop.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listcategory.ListCategory
import com.dennydev.dshop.model.productresponse.Product
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.sliderresponse.SliderResponse
import com.dennydev.dshop.network.ApiHomeImpl
import com.dennydev.dshop.network.ApiProductImpl
import com.dennydev.dshop.pagingsource.ProductDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class MainRepository @Inject constructor(val client: HttpClient) {
    fun getProduct(query: String): Flow<PagingData<Product>> {
        return Pager(PagingConfig(pageSize = 10)) {
            ProductDataSource(client, query)
        }.flow
    }
    suspend fun getSlider(): ApiResponse<SliderResponse> {
        return ApiHomeImpl(client).getSlider()
    }
    suspend fun getNewestProduct(): ApiResponse<ProductResponse>{
        return ApiHomeImpl(client).getNewestProduct()
    }

    suspend fun getCategory(): ApiResponse<ListCategory>{
        return ApiHomeImpl(client).getCategory()
    }
}