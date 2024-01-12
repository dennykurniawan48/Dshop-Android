package com.dennydev.dshop.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val confirmpass: String
)
