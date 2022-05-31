package com.example.eshop.data.repository

import com.example.eshop.data.remote.Constants.DATE
import com.example.eshop.data.remote.Constants.POPULARITY
import com.example.eshop.data.remote.Constants.RATING
import com.example.eshop.data.remote.RemoteDataSource
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Product
import com.example.eshop.di.IoDispatcher
import com.example.eshop.utils.Result
import com.example.eshop.utils.requestFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: RemoteDataSource
) {


    suspend fun getNewestProducts(perPage: Int): Flow<Result<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(DATE, perPage) }
    }

    suspend fun getMostViewedProducts(perPage: Int): Flow<Result<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(POPULARITY, perPage) }
    }

    suspend fun getBestSalesProducts(perPage: Int): Flow<Result<List<Product>>> {
        return requestFlow(dispatcher) { remoteDataSource.getProducts(RATING, perPage) }
    }

    fun getCategories(
        result: (
            clothing: Flow<Result<List<Category>>>,
            digital: Flow<Result<List<Category>>>,
            superMarket: Flow<Result<List<Category>>>,
            booksAndArt: Flow<Result<List<Category>>>
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

    suspend fun getProduct(id: Int): Flow<Result<Product>> =
        requestFlow(dispatcher) { remoteDataSource.getProduct(id) }

    suspend fun getProductsByCategory(categoryId: Int): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.getProductsByCategory(categoryId) }

    suspend fun searchProducts(searchText: String,orderBy: String): Flow<Result<List<Product>>> =
        requestFlow(dispatcher) { remoteDataSource.searchProducts(searchText, 100, orderBy) }
}