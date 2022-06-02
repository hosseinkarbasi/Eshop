package com.example.eshop.data.local.model

data class LocalCategory(
    val id: Int,
    val name: String,
    val image: LocalProductImage,
    val count: Int,
    val description: String,
    val display: String,
    val menu_order: Int,
    val parent: Int,
    val slug: String
)