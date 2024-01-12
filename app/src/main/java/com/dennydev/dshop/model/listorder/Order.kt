package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val address: String,
    val couponCode: String,
    val createdAt: String,
    val deliveryCost: Double,
    val deliveryId: String,
    val detailorder: List<Detailorder>,
    val discount: Double,
    val fullName: String,
    val id: String,
    val paid: Boolean,
    val stateId: String,
    val status: Status,
    val statusId: String,
    val total: Double,
    val updatedAt: String,
    val userId: String,
    val zipCode: String
)