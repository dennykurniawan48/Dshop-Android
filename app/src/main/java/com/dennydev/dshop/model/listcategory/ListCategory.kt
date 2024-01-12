package com.dennydev.dshop.model.listcategory

import kotlinx.serialization.Serializable

@Serializable
data class ListCategory(
    val `data`: List<Data>
)