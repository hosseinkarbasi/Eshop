package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class LineItem(
    @SN("product_id")
    val productId: Int,
    val quantity: Int
)