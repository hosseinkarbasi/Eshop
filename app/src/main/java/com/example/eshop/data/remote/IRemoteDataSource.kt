package com.example.eshop.data.remote

import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import retrofit2.Response

interface IRemoteDataSource {

    suspend fun getProducts(order: String, perPage: Int): Response<List<Product>>
    suspend fun getProduct(id: Int): Response<Product>
    suspend fun getCategories(categoryId: Int): Response<List<Category>>
    suspend fun getProductsByCategory(categoryId: Int): Response<List<Product>>
    suspend fun setOrder(order: Order): Response<Order>
    suspend fun getCustomer(email: String): Response<User>
    suspend fun searchProducts(
        searchText: String,
        perPage: Int,
        orderBy: String,
        order: String
    ): Response<List<Product>>

}