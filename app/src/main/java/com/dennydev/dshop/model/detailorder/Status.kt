package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: String,
    val status_name: String
)