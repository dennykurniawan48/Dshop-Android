package com.dennydev.dshop.model.listfavorite

import kotlinx.serialization.Serializable

@Serializable
data class ListFavorite(
    val `data`: List<Data>
)