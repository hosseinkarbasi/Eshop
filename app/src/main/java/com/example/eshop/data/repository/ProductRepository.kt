package com.example.eshop.data.repository

import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.POPULARITY
import com.example.eshop.application.Constants.RATING
import com.example.eshop.data.local.ILocalDataSource
import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.eshop.data.remote.ResultWrapper

@Singleton
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource
) {

    suspend fun getNewestProducts(perPage: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(DATE, perPage) }
    }

    suspend fun getMostViewedProducts(perPage: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(POPULARITY, perPage) }
    }

    suspend fun getBestSalesProducts(perPage: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(RATING, perPage) }
    }

    suspend fun getProduct(id: Int): Flow<ResultWrapper<Product>> =
        requestFlow(dispatcher) { remoteDataSource.getProduct(id) }

    suspend fun getProductsByCategory(categoryId: Int): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getProductsByCategory(categoryId) }

    suspend fun searchProducts(
        searchText: String,
        orderBy: String,
        order: String
    ): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.searchProducts(searchText, 100, orderBy, order) }

    suspend fun setOrder(order: Order): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.setOrder(order) }


    //local
    suspend fun insertProduct(product: LocalProduct) {
        withContext(dispatcher) {
            localDataSource.insertProduct(product)
        }
    }

    suspend fun update(product: LocalProduct) {
        withContext(dispatcher) {
            localDataSource.updateProduct(product)
        }
    }

    suspend fun deleteProduct(id: Int) {
        withContext(dispatcher) {
            localDataSource.deleteProduct(id)
        }
    }

    fun getLocalProducts(): Flow<List<LocalProduct>> = localDataSource.gerProducts()

    suspend fun deleteAllProductsBasket() =
        withContext(dispatcher) { localDataSource.deleteProduct() }
}