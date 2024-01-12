package com.dennydev.dshop.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennydev.dshop.model.productcategory.Product
import com.dennydev.dshop.pagingsource.ProductCategoryDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ProductCategoryRepository @Inject constructor(val client: HttpClient) {
    fun getProductByCategory(idCategory: String): Flow<PagingData<Product>> {
        return Pager(PagingConfig(pageSize = 10)) {
            ProductCategoryDataSource(client, idCategory)
        }.flow
    }
}