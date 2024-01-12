package com.dennydev.dshop.model.review

import kotlinx.serialization.Serializable

@Serializable
data class Star(
    val count: Int,
    val rating: Int
)