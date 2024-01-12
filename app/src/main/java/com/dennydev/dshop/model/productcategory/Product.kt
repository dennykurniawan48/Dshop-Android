package com.dennydev.dshop.model.productcategory

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val averageRating: Double,
    val categoryId: String,
    val id: String,
    val image1: String,
    val name: String,
    val price: Double
)