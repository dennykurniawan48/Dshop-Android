package com.dennydev.dshop.model.listfavorite

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val product: Product,
    val productId: String,
    val userId: String
)