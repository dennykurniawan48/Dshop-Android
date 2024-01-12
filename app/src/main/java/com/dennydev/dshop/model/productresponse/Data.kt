package com.dennydev.dshop.model.productresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val currentPage: Int,
    val products: List<Product>,
    val total: Int,
    val totalPage: Int
)