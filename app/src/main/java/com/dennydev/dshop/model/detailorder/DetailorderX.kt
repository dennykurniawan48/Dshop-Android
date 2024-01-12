package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class DetailorderX(
    val addReview: Boolean,
    val id: String,
    val orderId: String,
    val price: Double,
    val productId: String,
    val qty: Int,
    val products: Products,
)