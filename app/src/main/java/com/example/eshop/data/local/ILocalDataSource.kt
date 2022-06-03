package com.example.eshop.data.local

import com.example.eshop.data.local.model.LocalProduct
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {

    fun gerProducts(): Flow<List<LocalProduct>>
    suspend fun insertProduct(product: LocalProduct)
    suspend fun deleteProduct(id: String)
    suspend fun updateProduct(product: LocalProduct)
    suspend fun deleteProduct()

}