package com.dennydev.dshop.model.logoutresponse

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val `data`: Data
)