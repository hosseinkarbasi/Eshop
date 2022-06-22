package com.example.eshop.data.repository

import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.POPULARITY
import com.example.eshop.application.Constants.RATING
import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.SetOrder
import retrofit2.Response

@Singleton
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {

    suspend fun updateOrder(orderId: Int, order: SetOrder): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.updateOrder(orderId, order) }

    suspend fun getOrders(customerId: Int, status: String): Flow<ResultWrapper<List<Order>>> =
        requestFlow(dispatcher) { remoteDataSource.getOrders(customerId, status) }

    suspend fun setOrder(order: SetOrder): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.setOrder(order) }

    suspend fun getLastProduct(perPage: Int, page: Int): Response<List<Product>> {
        return remoteDataSource.getProducts(DATE, perPage, page)
    }

    suspend fun getNewestProducts(perPage: Int, page: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(DATE, perPage, page) }
    }

    suspend fun getMostViewedProducts(perPage: Int, page: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(POPULARITY, perPage, page) }
    }

    suspend fun getBestSalesProducts(perPage: Int, page: Int): Flow<ResultWrapper<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(RATING, perPage, page) }
    }

    suspend fun getProduct(id: Int): Flow<ResultWrapper<Product>> =
        requestFlow(dispatcher) { remoteDataSource.getProduct(id) }

    suspend fun getProductsList(ids: Array<Int>): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) {
            remoteDataSource.getProductsById(
                ids.contentToString().replace("[", "[0, ")
            )
        }

    suspend fun getProductsByCategory(
        categoryId: Int,
        perPage: Int,
        page: Int
    ): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) {
            remoteDataSource.getProductsByCategory(
                categoryId,
                perPage,
                page
            )
        }

    suspend fun searchProducts(
        searchText: String,
        orderBy: String,
        order: String
    ): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.searchProducts(searchText, 100, orderBy, order) }

}