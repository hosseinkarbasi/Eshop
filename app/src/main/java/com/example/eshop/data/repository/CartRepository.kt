package com.example.eshop.data.repository

import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Coupon
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.model.SetOrder
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {

    suspend fun getOrders(customerId: Int, status: String): Flow<ResultWrapper<List<Order>>> =
        requestFlow(dispatcher) { remoteDataSource.getOrders(customerId, status) }

    suspend fun getCoupon(code: String): Flow<ResultWrapper<List<Coupon>>> =
        requestFlow(dispatcher) { remoteDataSource.getCoupon(code) }

    suspend fun setOrder(order: SetOrder): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.setOrder(order) }

    suspend fun updateOrder(orderId: Int, order: SetOrder): Flow<ResultWrapper<Order>> =
        requestFlow(dispatcher) { remoteDataSource.updateOrder(orderId, order) }

    suspend fun getProductsList(ids: Array<Int>): Flow<ResultWrapper<List<Product>>> =
        requestFlow(dispatcher) {
            remoteDataSource.getProductsById(
                ids.contentToString().replace("[", "[0, ")
            )
        }
}