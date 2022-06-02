package com.example.eshop.data.local

import com.example.eshop.data.local.db.ProductDao
import com.example.eshop.data.local.model.LocalProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: ProductDao) {

    fun gerProducts(): Flow<List<LocalProduct>> = dao.getProducts()
    suspend fun insertProduct(product: LocalProduct) = dao.insertProduct(product)
    suspend fun deleteProduct(id: String) = dao.deleteProduct(id)
    suspend fun updateProduct(product: LocalProduct) = dao.updateProduct(product)

}