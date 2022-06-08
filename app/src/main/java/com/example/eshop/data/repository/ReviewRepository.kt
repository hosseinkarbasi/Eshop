package com.example.eshop.data.repository

import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Review
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {

    suspend fun getReviews(
        productId: Int,
        perPage: Int,
        page: Int
    ): Flow<ResultWrapper<List<Review>>> =
        requestFlow(dispatcher) { remoteDataSource.getReviews(productId, perPage, page) }


}