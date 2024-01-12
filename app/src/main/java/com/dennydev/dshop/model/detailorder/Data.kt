package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val address: String,
    val couponCode: String,
    val createdAt: String,
    val deliveryCost: Double,
    val deliveryId: String,
    val delivery: Delivery,
    val detailorder: List<DetailorderX>,
    val discount: Double,
    val fullName: String,
    val id: String,
    val paid: Boolean,
    val state: State,
    val stateId: String,
    val status: Status,
    val total: Double,
    val updatedAt: String,
    val userId: String,
    val zipCode: String
)