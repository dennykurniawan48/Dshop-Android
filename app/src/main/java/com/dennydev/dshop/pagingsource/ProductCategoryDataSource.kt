package com.dennydev.dshop.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennydev.dshop.model.productcategory.Product
import com.dennydev.dshop.model.productcategory.ProductCategory
import com.dennydev.dshop.network.ApiProductCategoryImpl
import com.dennydev.dshop.network.ApiProductImpl
import io.ktor.client.HttpClient

class ProductCategoryDataSource(val client: HttpClient, val idCategory: String): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 1
            val response: ProductCategory = ApiProductCategoryImpl(client).getProductByCategory(page, params.loadSize, idCategory)
            LoadResult.Page(
                data = response.data.products,
                prevKey = if(page==1) null else page-1,
                nextKey = if(page==response.data.totalPage) null else page+1
            )
        } catch (e: Exception) {
            Log.d("data", e.message.toString())
            LoadResult.Error(e)
        }
    }
}