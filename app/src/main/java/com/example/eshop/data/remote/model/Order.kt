package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Order(
    val id: Int,
    @SN("customer_id")
    val customerId: Int,
    @SN("line_items")
    val lineItems: List<LineItem>,
    val number: String,
    val total: String?,
    val status: String,
    @SN("date_created")
    val dateCreated: String
)