package com.dennydev.dshop.model.detailorder

import kotlinx.serialization.Serializable

@Serializable
data class State(
    val country: Country,
    val countryId: String,
    val id: String,
    val name: String
)