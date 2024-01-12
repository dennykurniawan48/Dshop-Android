package com.dennydev.dshop.model.listcategory

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val name: String,
    val thumbnail: String
)