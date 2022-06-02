package com.example.eshop.data.remote.model


data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val regular_price: String,
    val average_rating: String,
    val rating_count: Int,
    val categories: List<Category>,
    val images: List<ProductImage>
    )