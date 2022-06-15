package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class SetOrder(
    val id: Int,
    @SN("customer_id")
    val customerId: Int,
    @SN("line_items")
    val lineItems: List<LineItem>,
    val status: String,
    @SN("coupon_lines")
    val couponLines: List<Coupon>
)