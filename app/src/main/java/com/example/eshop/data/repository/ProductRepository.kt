package com.example.eshop.data.repository

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

    fun getItem(
        result: (
            new: Flow<Result<List<Product>>>,
            most: Flow<Result<List<Product>>>,
            best: Flow<Result<List<Product>>>
        ) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val newest = async {
                requestFlow(dispatcher) { remoteDataSource.getx("date") }
            }
            val viewed = async {
                requestFlow(dispatcher) { remoteDataSource.getx("popularity") }
            }
            val sales = async {
                requestFlow(dispatcher) { remoteDataSource.getx("rating") }
            }

            val new = newest.await()
            val most = viewed.await()
            val best = sales.await()
            result(new, most, best)
        }
    }


    suspend fun getProduct(id: String): Flow<Result<Product>> =
        requestFlow(dispatcher) { remoteDataSource.getProduct(id) }

    suspend fun getNewestProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getNewestProducts() }

    suspend fun getMostViewedProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getMostViewedProducts() }

    suspend fun getMostSalesProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getMostSalesProducts() }

    suspend fun getCategories(): Flow<Result<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories() }
}