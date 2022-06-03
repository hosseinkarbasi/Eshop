package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Category(
    val id: Int,
    val name: String,
    val image: ProductImage,
    val count: Int,
    val description: String,
    val display: String,
    @SN("menu_order")
    val menuOrder: Int,
    val parent: Int,
    val slug: String
)