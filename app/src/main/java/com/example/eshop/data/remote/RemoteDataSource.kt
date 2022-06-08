package com.example.eshop.data.remote

import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.model.*
import com.example.eshop.data.remote.network.WooCommerceApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val service: WooCommerceApi) :
    IRemoteDataSource {

    override suspend fun getCategories(categoryId: Int): Response<List<Category>> =
        service.getCategories(categoryId)

    override suspend fun getCoupon(code: String): Response<List<Coupon>> =
        service.getCoupon(code)

    override suspend fun getProduct(id: Int): Response<Product> =
        service.getProduct(id)

    override suspend fun setOrder(order: Order): Response<Order> =
        service.setOrder(order)

    override suspend fun getCustomer(email: String): Response<List<User>> =
        service.getCustomer(email)

    override suspend fun createCustomer(user: User): Response<User> =
        service.createCustomer(user)

    override suspend fun getOrders(customerId: Int): Response<List<Order>> =
        service.getOrders(customerId)

    override suspend fun getReviews(
        productId: Int,
        perPage: Int,
        page: Int
    ): Response<List<Review>> =
        service.getReviews(productId, perPage, page)


    override suspend fun getProductsByCategory(
        categoryId: Int,
        perPage: Int,
        page: Int
    ): Response<List<Product>> =
        service.getProductsByCategory(categoryId, perPage, page)

    override suspend fun getProducts(
        order: String,
        perPage: Int,
        page: Int
    ): Response<List<Product>> =
        service.getProducts(order, perPage, page)

    override suspend fun searchProducts(
        searchText: String,
        perPage: Int,
        orderBy: String,
        order: String
    ): Response<List<Product>> =
        service.searchProducts(searchText, perPage, orderBy, order)

}