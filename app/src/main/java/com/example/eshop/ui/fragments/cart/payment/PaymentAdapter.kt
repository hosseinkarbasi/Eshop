package com.example.eshop.ui.fragments.cart.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.databinding.ProductPaymentItemBinding

class PaymentAdapter : ListAdapter<LocalProduct, PaymentAdapter.CustomViewHolder>(DiffCallBack()) {

    inner class CustomViewHolder(private var binding: ProductPaymentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocalProduct) = binding.apply {
            Glide.with(root)
                .load(item.images)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(productImage)

            productQuantity.text = item.lineItems[layoutPosition].quantity.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            ProductPaymentItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<LocalProduct>() {
        override fun areItemsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocalProduct, newItem: LocalProduct) =
            oldItem == newItem
    }
}

