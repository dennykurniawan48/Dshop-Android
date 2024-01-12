package com.dennydev.dshop.profileresponse

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val `data`: Data
)