package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Review(
    val date_created: String,
    val date_created_gmt: String,
    val id: Int,
    @SN("product_id")
    val productId: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    @SN("reviewer_email")
    val reviewerEmail: String,
    val status: String,
    val verified: Boolean
)