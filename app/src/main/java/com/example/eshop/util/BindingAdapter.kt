package com.example.eshop.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imgUrl")
fun ImageView.setImage(url: String?) {
    Glide
        .with(this)
        .load(url)
        .into(this)
}