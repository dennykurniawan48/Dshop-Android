package com.dennydev.dshop.model.review

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val image: String? = null,
    val name: String
)