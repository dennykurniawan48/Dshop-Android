package com.dennydev.dshop.helper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dennydev.dshop.dao.CartDao
import com.dennydev.dshop.dao.OrderCart

@Database(entities = [OrderCart::class], version = 1)
abstract class DshopDatabase: RoomDatabase(){
    abstract fun cartDao(): CartDao
}