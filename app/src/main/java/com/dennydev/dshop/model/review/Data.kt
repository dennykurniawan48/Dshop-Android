package com.dennydev.dshop.model.review

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val review: List<Review>,
    val star: List<Star>,
    val total: Int
)