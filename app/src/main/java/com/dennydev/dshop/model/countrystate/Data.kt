package com.dennydev.dshop.model.countrystate

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val name: String,
    val state: List<State>
)