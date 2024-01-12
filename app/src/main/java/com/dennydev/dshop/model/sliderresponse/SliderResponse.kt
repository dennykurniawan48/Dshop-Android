package com.dennydev.dshop.model.sliderresponse

import kotlinx.serialization.Serializable

@Serializable
data class SliderResponse(
    val `data`: List<Data>
)