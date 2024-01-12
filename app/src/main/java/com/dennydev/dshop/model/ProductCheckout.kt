package com.dennydev.dshop.model

data class ProductCheckout(
    val productId: String,
    val price: Double,
    val qty:Int,
    val productName: String,
)
