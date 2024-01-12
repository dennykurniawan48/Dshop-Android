package com.dennydev.dshop.model.delivery

import kotlinx.serialization.Serializable

@Serializable
data class Delivery(
    val `data`: List<Data>
)