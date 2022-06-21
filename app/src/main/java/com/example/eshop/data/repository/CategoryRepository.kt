package com.example.eshop.data.repository

import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {
    suspend fun getClothingList(): Flow<ResultWrapper<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories(62) }

    suspend fun getDigitalList(): Flow<ResultWrapper<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories(52) }

    suspend fun getSuperMarketList(): Flow<ResultWrapper<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories(81) }

    suspend fun getBooksAndArtList(): Flow<ResultWrapper<List<Category>>> =
        requestFlow(dispatcher) { remoteDataSource.getCategories(76) }
}