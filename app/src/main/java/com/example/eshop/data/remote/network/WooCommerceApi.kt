package com.example.eshop.data.remote.network

import com.example.eshop.data.remote.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface WooCommerceApi {

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

}