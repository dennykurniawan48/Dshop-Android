package com.dennydev.dshop.model.countrystate

import kotlinx.serialization.Serializable

@Serializable
data class CountryState(
    val `data`: List<Data>
)