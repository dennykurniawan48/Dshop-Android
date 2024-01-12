package com.dennydev.dshop.network

import android.util.Log
import com.dennydev.dshop.model.Constant
import com.dennydev.dshop.model.productcategory.ProductCategory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ApiProductCategoryImpl(val client: HttpClient): ApiProductCategory {
    override suspend fun getProductByCategory(page: Int, limit: Int, idCategory: String): ProductCategory {
        val response: HttpResponse = client.get(
            Constant.PRODUCT_CATEGORY_URL.replace("{page}", page.toString())
                .replace("{limit}", "5")
                .replace("{cat}", idCategory)
        )
        Log.d("response", response.body())
        return response.body()
    }
}