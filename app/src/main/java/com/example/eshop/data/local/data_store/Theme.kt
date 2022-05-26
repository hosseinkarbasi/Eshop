package com.example.eshop.data.local.data_store

import androidx.appcompat.app.AppCompatDelegate

enum class Theme(
    val mode: Int
) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    NIGHT(AppCompatDelegate.MODE_NIGHT_YES),
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
}