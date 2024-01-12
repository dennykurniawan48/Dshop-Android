package com.dennydev.dshop.model.listorder

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: String,
    val status_name: String
)