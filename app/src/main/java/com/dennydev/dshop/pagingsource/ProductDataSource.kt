package com.dennydev.dshop.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.productresponse.Product as DataProduct
import com.dennydev.dshop.network.ApiProductImpl
import io.ktor.client.HttpClient

class ProductDataSource(val client: HttpClient, val query: String): PagingSource<Int, DataProduct>() {
    override fun getRefreshKey(state: PagingState<Int, DataProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataProduct> {
        return try {
            val page = params.key ?: 1
            val response: ProductResponse = ApiProductImpl(client).getProduct(page, params.loadSize, query)
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