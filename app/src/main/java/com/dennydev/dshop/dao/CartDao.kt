package com.dennydev.dshop.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartOrder: OrderCart)

    @Query("SELECT * FROM carts")
    fun getAllCart(): Flow<List<OrderCart>>

    @Query("SELECT * FROM carts WHERE productId = :id")
    fun checkItemCount(id: String): Flow<List<OrderCart>>

    @Update
    suspend fun updateCart(cartOrder: OrderCart)

    @Query("DELETE FROM carts")
    suspend fun deleteAllCart()

    @Query("DELETE FROM carts WHERE productId=:productid")
    suspend fun deleteFromcart(productid: String)

    @Query("UPDATE carts SET qty=qty+1 WHERE productId=:productId")
    suspend fun increaseItem(productId: String)

    @Query("UPDATE carts SET qty=qty-1 WHERE productId=:productId")
    suspend fun decreaseItem(productId: String)
}