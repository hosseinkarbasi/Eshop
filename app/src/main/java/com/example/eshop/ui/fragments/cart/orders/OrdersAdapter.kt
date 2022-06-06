package com.example.eshop.ui.fragments.cart.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eshop.data.remote.model.Order
import com.example.eshop.databinding.OrdersItemBinding

class OrdersAdapter :
    ListAdapter<Order, OrdersAdapter.CustomViewHolder>(DiffCallBack()) {

    inner class CustomViewHolder(private var binding: OrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order) = binding.apply {

            numberOrder.text = item.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            OrdersItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order) =
            oldItem.customerId == newItem.customerId

        override fun areContentsTheSame(oldItem: Order, newItem: Order) =
            oldItem == newItem
    }
}

