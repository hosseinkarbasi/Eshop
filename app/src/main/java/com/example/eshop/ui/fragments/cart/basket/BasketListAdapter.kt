package com.example.eshop.ui.fragments.cart.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.databinding.BasketListItemBinding

class BasketListAdapter :
    ListAdapter<LocalProduct, BasketListAdapter.CustomViewHolder>(DiffCallBack()) {

    private var itemClick: ((product: LocalProduct) -> Unit)? = null
    private var itemIncrease: ((counter: LocalProduct, position: Int) -> Unit)? = null
    private var itemDecrease: ((counter: LocalProduct, position: Int) -> Unit)? = null

    inner class CustomViewHolder(private var binding: BasketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClick?.let {
                    it(getItem(bindingAdapterPosition))
                }
            }

            binding.plus.setOnClickListener {
                itemIncrease?.let {
                    it(getItem(bindingAdapterPosition), bindingAdapterPosition)
                }
            }

            binding.minus.setOnClickListener {
                itemDecrease?.let {
                    it(getItem(bindingAdapterPosition), bindingAdapterPosition)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: LocalProduct) = binding.apply {
            counter.text = item.quantity.toString()
            productTitle.text = item.name
            productPrice.text = " ${item.price.toInt() * item.quantity}  تومان "
            Glide.with(root)
                .load(item.images)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(productImage)
            if (item.quantity == 1){
                minus.setIconResource(R.drawable.ic_baseline_delete)
            }else minus.setIconResource(R.drawable.ic_baseline_remove)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            BasketListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    fun onItemPosition(clickListener: (LocalProduct) -> Unit) {
        itemClick = clickListener
    }

    fun onItemIncrease(clickListener: (LocalProduct, Int) -> Unit) {
        itemIncrease = clickListener
    }

    fun onItemDecrease(clickListener: (LocalProduct, Int) -> Unit) {
        itemDecrease = clickListener
    }

    class DiffCallBack : DiffUtil.ItemCallback<LocalProduct>() {
        override fun areItemsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem == newItem
    }
}

