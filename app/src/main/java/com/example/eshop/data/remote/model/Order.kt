package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Order(
    @SN("customer_id")
    val customerId: Int,
    @SN("line_items")
    val lineItems: List<LineItem>,
    val number: String
)