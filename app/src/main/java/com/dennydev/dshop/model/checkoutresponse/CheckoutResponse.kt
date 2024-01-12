package com.dennydev.dshop.model.checkoutresponse

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val `data`: Data
)