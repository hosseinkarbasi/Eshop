package com.example.eshop.data.remote.network

import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import retrofit2.Response
import retrofit2.http.*

interface WooCommerceApi {

    @GET("products")
    suspend fun getProducts(
        @Query("orderby") orderBy: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Product>>

    @GET("products/categories")
    suspend fun getCategories(@Query("parent") parentId: Int): Response<List<Category>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<Product>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Product>>

    @GET("products")
    suspend fun searchProducts(
        @Query("search") searchText: String,
        @Query("per_page") perPage: Int,
        @Query("orderby") orderBy: String,
        @Query("order") order: String
    ): Response<List<Product>>

    @POST("orders")
    suspend fun setOrder(@Body order: Order): Response<Order>

    @GET("customers")
    suspend fun getCustomer(@Query("email") email: String): Response<List<User>>

    @POST("customers")
    suspend fun createCustomer(@Body user: User): Response<User>

    @GET("orders")
    suspend fun getOrders(@Query("customer") customerId: Int): Response<List<Order>>

}