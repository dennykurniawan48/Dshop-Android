package com.dennydev.dshop.model.sliderresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val image: String
)