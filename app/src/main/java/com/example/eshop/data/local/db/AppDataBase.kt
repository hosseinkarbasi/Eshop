package com.example.eshop.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eshop.data.local.model.LocalProduct

@Database(entities = [LocalProduct::class], version = 1, exportSchema = true)
abstract class AppDataBase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
}