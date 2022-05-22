package com.example.eshop.data.remote

import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.network.WooCommerceApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: WooCommerceApi) {

    suspend fun getNewestProducts(): Response<List<Product>> = service.getProducts("date")
    suspend fun getMostViewedProducts(): Response<List<Product>> = service.getProducts("popularity")
    suspend fun getMostSalesProducts(): Response<List<Product>> = service.getProducts("rating")

}