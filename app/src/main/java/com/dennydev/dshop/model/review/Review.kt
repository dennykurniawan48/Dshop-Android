package com.dennydev.dshop.model.review

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val comment: String,
    val rating: Int,
    val user: User
)