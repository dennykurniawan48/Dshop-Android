package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Categories(
    val id: String,
    val name: String,
    val thumbnail: String
)