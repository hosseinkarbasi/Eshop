package com.example.eshop.data.local.model

import com.example.eshop.data.remote.model.LineItem

data class LocalProduct(

    val id: Int,
    val name: String,
    val price: String,
    val images: String,
    var lineItems: List<LineItem>
)