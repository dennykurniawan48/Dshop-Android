package com.dennydev.dshop.model.loginresponse

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val `data`: Data
)