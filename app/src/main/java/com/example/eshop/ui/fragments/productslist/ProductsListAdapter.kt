package com.example.eshop.ui.fragments.productslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eshop.R
import com.example.eshop.data.remote.model.Product
import com.example.eshop.databinding.ProductsListItemBinding

class ProductsListAdapter :
    ListAdapter<Product, ProductsListAdapter.CustomViewHolder>(DiffCallBack()) {

    private var itemClick: ((product: Product) -> Unit)? = null

    inner class CustomViewHolder(private var binding: ProductsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Product) = binding.apply {
            productTitle.text = item.name
            productPrice.text = " ${item.price} تومان "
            Glide.with(root)
                .load(item.images[0].src)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(productImage)

            binding.root.setOnClickListener {
                itemClick?.let {
                    it(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            ProductsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun onItemPosition(clickListener: (Product) -> Unit) {
        itemClick = clickListener
    }

    class DiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }
}

