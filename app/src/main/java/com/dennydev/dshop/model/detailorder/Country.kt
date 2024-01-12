package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: String,
    val name: String
)