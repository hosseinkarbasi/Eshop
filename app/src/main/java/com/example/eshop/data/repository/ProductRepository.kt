package com.example.eshop.data.repository

import com.example.eshop.data.remote.RemoteDataSource
import com.example.eshop.data.remote.model.Product
import com.example.eshop.di.IoDispatcher
import com.example.eshop.util.Result
import com.example.eshop.util.requestFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getNewestProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getNewestProducts() }

    suspend fun getMostViewedProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getMostViewedProducts() }

    suspend fun getMostSalesProducts(): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getMostSalesProducts() }
}