package com.dennydev.dshop.model.loginresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val accessToken: String,
    val age: Int? = null,
    val bio: String? = null,
    val email: String,
    val emailVerified: String? = null,
    val id: String,
    val image: String? = null,
    val isAdmin: Boolean,
    val name: String,
    val expiresIn: Int,
    val google: Boolean
)