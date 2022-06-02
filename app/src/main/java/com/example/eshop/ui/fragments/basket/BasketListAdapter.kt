package com.example.eshop.ui.fragments.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.databinding.ProductsListItemBinding

class BasketListAdapter :
    ListAdapter<LocalProduct, BasketListAdapter.CustomViewHolder>(DiffCallBack()) {

    private var itemClick: ((product: LocalProduct) -> Unit)? = null

    inner class CustomViewHolder(private var binding: ProductsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LocalProduct) = binding.apply {
            productTitle.text = item.name
            productPrice.text = " ${item.price} تومان "
            Glide.with(root)
                .load(item.images)
                .placeholder(R.drawable.online_shopping_palceholder)
                .transition(DrawableTransitionOptions.withCrossFade())
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

    fun onItemPosition(clickListener: (LocalProduct) -> Unit) {
        itemClick = clickListener
    }

    class DiffCallBack : DiffUtil.ItemCallback<LocalProduct>() {
        override fun areItemsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem == newItem
    }
}

