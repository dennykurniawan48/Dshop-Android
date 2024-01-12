package com.dennydev.dshop.model.delivery

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val duration: String,
    val id: String,
    val name: String,
    val price: Double
)