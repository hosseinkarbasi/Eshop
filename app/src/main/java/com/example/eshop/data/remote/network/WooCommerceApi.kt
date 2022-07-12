package com.example.eshop.data.remote.network

import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface WooCommerceApi {

    @GET("products/categories")
    suspend fun getCategories(@Query("parent") parentId: Int): Response<List<Category>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<Product>

    @POST("orders")
    suspend fun setOrder(@Body order: SetOrder): Response<Order>

    @GET("customers")
    suspend fun getCustomer(@Query("email") email: String): Response<List<User>>

    @POST("customers")
    suspend fun createCustomer(@Body user: User): Response<User>

    @POST("orders/{id}")
    suspend fun updateOrder(@Path("id") orderId: Int, @Body order: SetOrder): Response<Order>

    @GET("coupons")
    suspend fun getCoupon(@Query("code") code: String): Response<List<Coupon>>

    @POST("products/reviews")
    suspend fun createReview(@Body review: Review): Response<Review>

    @GET("products")
    suspend fun getProductsById(@Query("include") ids: String): Response<List<Product>>

    @POST("products/reviews/{id}")
    suspend fun editReview(@Path("id") reviewId: Int, @Body review: Review): Response<Review>

    @Headers("Api-Key: service.UyZbZ9QzO6ngD5BPzZGmL2QFGsZCjHmdE1b9qCyI")
    @GET("https://api.neshan.org/v4/reverse")
    suspend fun getAddress(
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?
    ): Response<NeshanAddress>

    @DELETE("products/reviews/{id}")
    suspend fun deleteReview(
        @Path("id") reviewId: Int,
        @Query("force") force: String
    ): Response<DeleteReview>

    @GET("orders")
    suspend fun getOrders(
        @Query("customer") customerId: Int,
        @Query("status") status: String
    ): Response<List<Order>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProducts(
        @Query("orderby") orderBy: String,
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

    @GET("products/reviews")
    suspend fun getReviews(
        @Query("product") productId: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Review>>

    @GET("products/reviews")
    suspend fun getUserReviews(
        @Query("reviewer_email") userEmail: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Review>>

}