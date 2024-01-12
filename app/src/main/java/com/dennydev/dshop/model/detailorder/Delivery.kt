package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class Delivery(
    val duration: String,
    val id: String,
    val name: String,
    val price: Double
)