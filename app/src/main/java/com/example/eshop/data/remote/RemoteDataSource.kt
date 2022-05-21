package com.example.eshop.data.remote

import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.network.WooCommerceApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: WooCommerceApi) {

    suspend fun getProducts(): Response<List<Product>> = service.getProducts()

}