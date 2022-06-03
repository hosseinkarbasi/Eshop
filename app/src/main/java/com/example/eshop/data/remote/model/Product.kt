package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    @SN("regular_price")
    val regularPrice: String,
    @SN("average_rating")
    val averageRating: String,
    @SN("rating_count")
    val ratingCount: Int,
    val categories: List<Category>,
    val images: List<ProductImage>
)