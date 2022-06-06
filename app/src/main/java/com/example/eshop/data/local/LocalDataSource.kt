package com.example.eshop.data.local

import com.example.eshop.data.local.db.ProductDao
import com.example.eshop.data.local.model.LocalProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: ProductDao) : ILocalDataSource {

    override fun gerProducts(): Flow<List<LocalProduct>> = dao.getProducts()
    override suspend fun insertProduct(product: LocalProduct) = dao.insertProduct(product)
    override suspend fun deleteProduct(id: Int) = dao.deleteProduct(id)
    override suspend fun updateProduct(product: LocalProduct) = dao.updateProduct(product)
    override suspend fun deleteProduct() = dao.deleteAllProducts()

}