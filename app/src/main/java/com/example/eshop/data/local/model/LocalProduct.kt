package com.example.eshop.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product")
data class LocalProduct(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val regular_price: String,
    val average_rating: String,
    val rating_count: Int,
    val categories: List<LocalCategory>,
    val images: List<LocalProductImage>
)