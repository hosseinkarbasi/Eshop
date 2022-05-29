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

    fun getProducts(
        result: (
            new: Flow<Result<List<Product>>>,
            most: Flow<Result<List<Product>>>,
            best: Flow<Result<List<Product>>>
        ) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val newest = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(DATE) }
            }
            val viewed = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(POPULARITY) }
            }
            val sales = async {
                requestFlow(dispatcher) { remoteDataSource.getProducts(RATING) }
            }

            val new = newest.await()
            val most = viewed.await()
            val best = sales.await()
            result(new, most, best)
        }
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
}