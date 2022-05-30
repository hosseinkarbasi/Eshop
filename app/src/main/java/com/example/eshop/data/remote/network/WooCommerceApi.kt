package com.example.eshop.data.remote.network

import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApi {

    @GET("products")
    suspend fun getProducts(@Query("orderby") orderBy: String): Response<List<Product>>

    suspend fun getProductsByPerPage(@Query("orderby") orderBy: String): Response<List<Product>>

    @GET("products/categories")
    suspend fun getCategories(@Query("parent") parentId: Int): Response<List<Category>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<Product>

    @GET("products")
    suspend fun getProductsByCategory(@Query("category") categoryId: Int): Response<List<Product>>

}