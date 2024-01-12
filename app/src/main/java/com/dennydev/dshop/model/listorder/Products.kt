package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Products(
    val categories: Categories,
    val image1: String,
    val name: String
)