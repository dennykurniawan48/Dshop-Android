package com.dennydev.dshop.model.registerresponse

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val age: Int? = null,
    val bio: String? = null,
    val email: String,
    val emailVerified: String?=null,
    val id: String,
    val image: String?=null,
    val isAdmin: Boolean,
    val name: String,
    val fcmToken: String?=null
)