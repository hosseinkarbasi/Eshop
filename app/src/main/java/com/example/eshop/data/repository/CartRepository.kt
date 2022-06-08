package com.example.eshop.data.repository

import com.example.eshop.data.local.LocalDataSource
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Coupon
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun getOrders(customerId: Int): Flow<ResultWrapper<List<Order>>> =
        requestFlow(dispatcher) { remoteDataSource.getOrders(customerId) }

    suspend fun getCoupon(code: String): Flow<ResultWrapper<List<Coupon>>> =
        requestFlow(dispatcher) { remoteDataSource.getCoupon(code) }

    suspend fun setOrder(order: Order): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.setOrder(order) }

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

    fun getLocalProducts(): Flow<List<LocalProduct>> =
        localDataSource.gerProducts()

    suspend fun deleteAllProductsBasket() =
        withContext(dispatcher) { localDataSource.deleteProduct() }

}