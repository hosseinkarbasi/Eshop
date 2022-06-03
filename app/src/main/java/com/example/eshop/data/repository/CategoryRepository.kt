package com.example.eshop.data.repository

import com.example.eshop.data.remote.IRemoteDataSource
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.requestFlow
import com.example.eshop.di.IoDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository@Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) {

    fun getCategories(
        result: (
            clothing: Flow<ResultWrapper<List<Category>>>,
            digital: Flow<ResultWrapper<List<Category>>>,
            superMarket: Flow<ResultWrapper<List<Category>>>,
            booksAndArt: Flow<ResultWrapper<List<Category>>>
        ) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val clothing = async {
                requestFlow(dispatcher) { remoteDataSource.getCategories(62) }
            }
            val digital = async {
                requestFlow(dispatcher) { remoteDataSource.getCategories(52) }
            }
            val superMarket = async {
                requestFlow(dispatcher) { remoteDataSource.getCategories(81) }
            }
            val booksAndArt = async {
                requestFlow(dispatcher) { remoteDataSource.getCategories(76) }
            }
            val clothingC = clothing.await()
            val digitalC = digital.await()
            val superMarketC = superMarket.await()
            val booksAndArtC = booksAndArt.await()
            result(clothingC, digitalC, superMarketC, booksAndArtC)
        }
    }

}