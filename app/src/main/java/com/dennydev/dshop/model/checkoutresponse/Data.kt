package com.dennydev.dshop.model.checkoutresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val address: String,
    val couponCode: String,
    val createdAt: String,
    val deliveryCost: Double,
    val deliveryId: String,
    val discount: Double,
    val fullName: String,
    val id: String,
    val paid: Boolean,
    val stateId: String,
    val statusId: String,
    val total: Double,
    val updatedAt: String,
    val userId: String,
    val zipCode: String
)