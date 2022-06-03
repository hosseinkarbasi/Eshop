package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class ProductImage(
    val alt: String,
    @SN("date_created")
    val dateCreated: String,
    @SN("date_created_gmt")
    val dateCreatedGmt: String,
    @SN("date_modified")
    val dateModified: String,
    @SN("date_modified_gmt")
    val dateModifiedGmt: String,
    val id: Int,
    val name: String,
    val src: String
)