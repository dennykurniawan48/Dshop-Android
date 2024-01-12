package com.dennydev.dshop.model.productresponse

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val `data`: Data
)