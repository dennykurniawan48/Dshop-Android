package com.dennydev.dshop.model.listfavorite

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val averageRating: Double? = null,
    val id: String,
    val image1: String,
    val name: String,
    val price: Double
)