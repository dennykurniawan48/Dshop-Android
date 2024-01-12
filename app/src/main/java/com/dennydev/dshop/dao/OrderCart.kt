package com.dennydev.dshop.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "carts")
data class OrderCart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: String,
    var qty: Int
)
