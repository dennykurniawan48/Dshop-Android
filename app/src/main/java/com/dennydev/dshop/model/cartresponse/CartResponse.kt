package com.dennydev.dshop.model.cartresponse

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val `data`: List<Data>
)