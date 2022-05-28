package com.example.eshop.utils

import android.widget.ImageView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("imgUrl")
fun ImageView.setImage(url: String?) {
    Glide
        .with(this)
        .load(url)
        .into(this)
}


@BindingAdapter("textSpan")
fun MaterialTextView.setTextDec(text: String) {
    this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
}