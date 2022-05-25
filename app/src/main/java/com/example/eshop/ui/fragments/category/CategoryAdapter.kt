package com.example.eshop.ui.fragments.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eshop.data.remote.model.Category
import com.example.eshop.databinding.ProductItemBinding

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.CustomViewHolder>(DiffCallBack()) {

    inner class CustomViewHolder(private var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) = binding.apply {
            productTitle.text = item.name
            productPrice.text = item.count.toString()
            Glide.with(root)
                .load(item.image.src)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}

