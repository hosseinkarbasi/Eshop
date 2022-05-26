package com.example.eshop.data.repository

import com.example.eshop.data.remote.Constants.DATE
import com.example.eshop.data.remote.Constants.POPULARITY
import com.example.eshop.data.remote.Constants.RATING
import com.example.eshop.data.remote.RemoteDataSource
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Product
import com.example.eshop.di.IoDispatcher
import com.example.eshop.util.Result
import com.example.eshop.util.requestFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: RemoteDataSource
) {

    fun getProducts(
        result: (
            new: Flow<Result<List<Product>>>,
            most: Flow<Result<List<Product>>>,
            best: Flow<Result<List<Product>>>
        ) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val newest = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(DATE) }
            }
            val viewed = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(POPULARITY) }
            }
            val sales = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(RATING) }
            }

            val new = newest.await()
            val most = viewed.await()
            val best = sales.await()
            result(new, most, best)
        }
    }

    suspend fun getProduct(id: Int): Flow<Result<Product>> =
        requestFlow(dispatcher) { remoteDataSource.getProduct(id) }

    suspend fun getCategories(): Flow<Result<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories() }
}