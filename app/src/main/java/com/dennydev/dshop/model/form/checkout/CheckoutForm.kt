package com.dennydev.dshop.model.form.checkout

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutForm(
    val address: String,
    val coupon: String,
    val deliveryCost: Double,
    val details: List<Detail>,
    val discount: Double,
    val shippingMethodId: String,
    val stateId: String,
    val total: Double,
    val zipCode: String
)