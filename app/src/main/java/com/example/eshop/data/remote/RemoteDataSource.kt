package com.example.eshop.data.remote

import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.network.WooCommerceApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: WooCommerceApi) {

    suspend fun getCategories(): Response<List<Category>> = service.getCategories(62)
    suspend fun getProducts(order: String): Response<List<Product>> = service.getProducts(order)
    suspend fun getProduct(id: Int): Response<Product> = service.getProduct(id)
    suspend fun getProductsByCategory(categoryId:Int): Response<List<Product>> = service.getProductsByCategory(categoryId)
}