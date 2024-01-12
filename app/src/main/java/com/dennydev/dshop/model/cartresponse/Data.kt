package com.dennydev.dshop.model.cartresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val availableStock: Int,
    val id: String,
    val image1: String,
    val isActive: Boolean,
    val name: String,
    val price: Double
)