package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class Products(
    val image1: String,
    val name: String
)