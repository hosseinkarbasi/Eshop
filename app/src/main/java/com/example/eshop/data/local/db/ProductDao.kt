package com.example.eshop.data.local.db

import androidx.room.*
import com.example.eshop.data.local.model.LocalProduct
import kotlinx.coroutines.flow.Flow

//@Dao
//interface ProductDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertProduct(product: LocalProduct)
//
//    @Query("DELETE FROM product WHERE id=:id")
//    suspend fun deleteProduct(id: Int)
//
//    @Query("DELETE FROM product")
//    suspend fun deleteAllProducts()
//
//    @Update
//    suspend fun updateProduct(product: LocalProduct)
//
//    @Query("SELECT * FROM product ")
//    fun getProducts(): Flow<List<LocalProduct>>
//}