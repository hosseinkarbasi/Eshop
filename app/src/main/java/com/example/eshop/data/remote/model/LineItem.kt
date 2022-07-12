package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class LineItem(
    val id: Int,
    val name : String,
    @SN("product_id")
    val productId: Int,
    var quantity: Int,
    @SN("meta_data")
    val metaData: List<MetaData>,
    val price: Double
)