package com.dennydev.dshop.repository

import android.util.Log
import com.dennydev.dshop.dao.CartDao
import com.dennydev.dshop.dao.OrderCart
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class CartOrderRepository @Inject constructor(private val cartDao: CartDao) {
    val cartData: Flow<List<OrderCart>> = cartDao.getAllCart()

    suspend fun addToCart(product: OrderCart) {
        Log.d("add item", product.toString())
        cartDao.addToCart(product)
    }

    suspend fun updateCart(product: OrderCart){
        cartDao.updateCart(product)
    }

    suspend fun deleteAllCart(){
        cartDao.deleteAllCart()
    }

    suspend fun deleteFromCart(productId: String){
        cartDao.deleteFromcart(productId)
    }

    fun checkItemFromCart(id: String): Flow<List<OrderCart>>{
        return cartDao.checkItemCount(id)
    }

    suspend fun increaseItem(productId: String){
        cartDao.increaseItem(productId)
    }

    suspend fun decreseItem(productId: String){
        cartDao.decreaseItem(productId)
    }
}