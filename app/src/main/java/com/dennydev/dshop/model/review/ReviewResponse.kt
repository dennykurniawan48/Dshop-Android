package com.dennydev.dshop.model.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val `data`: Data
)