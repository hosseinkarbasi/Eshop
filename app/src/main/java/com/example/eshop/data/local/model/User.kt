package com.example.eshop.data.local.model

import com.google.gson.annotations.SerializedName as SN

data class User(
    val id: Int,
    @SN("first_name")
    val firstName: String,
    @SN("last_name")
    val lastName: String,
    val email: String
)