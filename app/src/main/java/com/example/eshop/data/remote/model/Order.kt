package com.example.eshop.data.remote.model

data class Order(
    val customer_id: Int,
    val line_items: List<LineItem>,
    val number: String
)