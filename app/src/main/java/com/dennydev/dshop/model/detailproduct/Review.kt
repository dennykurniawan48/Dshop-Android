package com.dennydev.dshop.model.detailproduct

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val comment: String,
    val rating: Int,
    val user: User
)