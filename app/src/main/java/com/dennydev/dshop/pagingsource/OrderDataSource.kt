package com.dennydev.dshop.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.listorder.Order
import com.dennydev.dshop.network.ApiProductImpl
import com.dennydev.dshop.network.ApiProfileImpl
import io.ktor.client.HttpClient

class OrderDataSource(val client: HttpClient, val token: String): PagingSource<Int, Order>() {
    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        return try {
            val page = params.key ?: 1
            val response: ListOrder = ApiProfileImpl(client).getProfileOrder(page, params.loadSize, token)
            LoadResult.Page(
                data = response.data.order,
                prevKey = if(page==1) null else page-1,
                nextKey = if(page==response.data.totalPage) null else page+1
            )
        } catch (e: Exception) {
            Log.d("data", e.message.toString())
            LoadResult.Error(e)
        }
    }
}