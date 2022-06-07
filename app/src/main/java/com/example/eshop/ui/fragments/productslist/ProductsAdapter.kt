package com.example.eshop.ui.fragments.productslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.data.remote.model.Product
import com.example.eshop.databinding.ProductsListItemBinding

class ProductsAdapter() : RecyclerView.Adapter<ProductsAdapter.UsersViewHolder>() {

    private var itemClick: ((product: Product) -> Unit)? = null
    var list = mutableListOf<Product>()

    inner class UsersViewHolder(val binding: ProductsListItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder =
        UsersViewHolder(
            ProductsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun onItemPosition(clickListener: (Product) -> Unit) {
        itemClick = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(items: List<Product>?) {
        if (items != null) {
            list.addAll(items)
        }
        notifyDataSetChanged()
    }
}
