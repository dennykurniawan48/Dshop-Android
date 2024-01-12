package com.dennydev.dshop.model.form.checkout

import kotlinx.serialization.Serializable

@Serializable
data class Detail(
    val productId: String,
    val qty: Int
)