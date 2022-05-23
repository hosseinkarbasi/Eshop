package com.example.eshop.data.remote.model

data class Category(
    val id: Int,
    val name: String,
    val image: ProductImage,
    val count: Int,
    val description: String,
    val display: String,
    val menu_order: Int,
    val parent: Int,
    val slug: String
)