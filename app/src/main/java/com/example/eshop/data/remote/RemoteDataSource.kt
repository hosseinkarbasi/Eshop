package com.example.eshop.data.remote

import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.network.WooCommerceApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: WooCommerceApi) :
    IRemoteDataSource {

    override suspend fun getCategories(categoryId: Int): Response<List<Category>> =
        service.getCategories(categoryId)

    override suspend fun getProducts(order: String, perPage: Int): Response<List<Product>> =
        service.getProducts(order, perPage)

    override suspend fun getProduct(id: Int): Response<Product> =
        service.getProduct(id)

    override suspend fun getProductsByCategory(categoryId: Int): Response<List<Product>> =
        service.getProductsByCategory(categoryId)

    override suspend fun setOrder(order: Order): Response<Order> =
        service.setOrder(order)

    override suspend fun searchProducts(
        searchText: String,
        perPage: Int,
        orderBy: String,
        order: String
    ): Response<List<Product>> =
        service.searchProducts(searchText, perPage, orderBy, order)

}