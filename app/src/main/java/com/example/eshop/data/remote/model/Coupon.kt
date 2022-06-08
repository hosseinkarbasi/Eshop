package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class Coupon(
    val id: Int,
    val code: String,
    val amount: String,
    @SN("maximum_amount")
    val maximumAmount: String,
    @SN("minimum_amount")
    val minimumAmount: String
)