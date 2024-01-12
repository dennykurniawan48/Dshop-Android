package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val currentPage: Int,
    val order: List<Order>,
    val total: Int,
    val totalPage: Int
)