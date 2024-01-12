package com.dennydev.dshop.model.detailproduct

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val availableStock: Int,
    val averageRating: Double?=null,
    val categoryId: String,
    val desc: String,
    val id: String,
    val image1: String,
    val image2: String,
    val image3: String,
    val image4: String,
    val isActive: Boolean,
    val name: String,
    val price: Double,
    //val reviews: List<Review>
)