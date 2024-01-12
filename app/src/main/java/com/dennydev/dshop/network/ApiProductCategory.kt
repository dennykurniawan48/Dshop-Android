package com.dennydev.dshop.network

import com.dennydev.dshop.model.productcategory.ProductCategory

interface ApiProductCategory {
    suspend fun getProductByCategory(page: Int, limit: Int, idCategory: String): ProductCategory
}