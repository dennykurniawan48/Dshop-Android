package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Detailorder(
    val price: Double,
    val products: Products,
    val qty: Int
)