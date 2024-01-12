package com.dennydev.dshop.profileresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val age: Int? = null,
    val bio: String? = null,
    val email: String,
    val emailVerified: String?=null,
    val exp: Long,
    val iat: Int,
    val id: String,
    val image: String?=null,
    val isAdmin: Boolean,
    val name: String
)