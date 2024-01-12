package com.dennydev.dshop.model.countrystate

import kotlinx.serialization.Serializable

@Serializable
data class State(
    val countryId: String,
    val id: String,
    val name: String
)