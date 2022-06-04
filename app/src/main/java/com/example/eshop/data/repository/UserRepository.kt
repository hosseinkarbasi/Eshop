package com.example.eshop.data.repository

import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {

    suspend fun getCustomer(email: String): Flow<ResultWrapper<User>> {
        return requestFlow(dispatcher) { remoteDataSource.getCustomer(email) }
    }

}