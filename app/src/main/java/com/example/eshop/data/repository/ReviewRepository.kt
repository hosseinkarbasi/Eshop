package com.example.eshop.data.repository

import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.DeleteReview
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

    suspend fun createReview(review: Review): Flow<ResultWrapper<Review>> =
        requestFlow(dispatcher) { remoteDataSource.createReview(review) }

    suspend fun deleteReview(reviewId: Int): Flow<ResultWrapper<DeleteReview>> =
        requestFlow(dispatcher) { remoteDataSource.deleteReview(reviewId, "true") }

    suspend fun editReview(reviewId: Int, review: Review): Flow<ResultWrapper<Review>> =
        requestFlow(dispatcher) { remoteDataSource.editReview(reviewId, review) }

    suspend fun getReviews(
        productId: Int,
        perPage: Int,
        page: Int
    ): Flow<ResultWrapper<List<Review>>> =
        requestFlow(dispatcher) { remoteDataSource.getReviews(productId, perPage, page) }

    suspend fun getUserReviews(
        userEmail: String,
        perPage: Int,
        page: Int
    ): Flow<ResultWrapper<List<Review>>> =
        requestFlow(dispatcher) { remoteDataSource.getUserReviews(userEmail, perPage, page) }

}