package com.example.eshop.ui.fragments.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.eshop.R
import com.example.eshop.data.remote.model.ProductImage
import com.example.eshop.databinding.ImageItemBinding

class ImageViewPagerAdapter :
    ListAdapter<ProductImage, ImageViewPagerAdapter.ViewPagerViewHolder>(DiffCallBack()) {

    inner class ViewPagerViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(image: ProductImage) {
            Glide.with(binding.root)
                .load(image.src)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(binding.productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
        ViewPagerViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<ProductImage>() {
        override fun areItemsTheSame(oldItem: ProductImage, newItem: ProductImage) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductImage, newItem: ProductImage) =
            oldItem == newItem
    }

}