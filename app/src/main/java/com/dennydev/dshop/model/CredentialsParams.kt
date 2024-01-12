package com.dennydev.dshop.model

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsParams(
    val email: String, val password: String, val fcmtoken: String? = null
)