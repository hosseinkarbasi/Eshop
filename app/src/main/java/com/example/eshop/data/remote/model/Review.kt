package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Review(
    val id: Int,
    @SN("date_created")
    val dateCreated: String,
    @SN("product_id")
    val productId: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    @SN("reviewer_email")
    val reviewerEmail: String,
)