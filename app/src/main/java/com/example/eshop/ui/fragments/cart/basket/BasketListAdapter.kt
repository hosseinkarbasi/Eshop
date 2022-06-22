package com.example.eshop.ui.fragments.cart.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.data.remote.model.LineItem
import com.example.eshop.databinding.BasketListItemBinding
import java.text.DecimalFormat

class BasketListAdapter :
    ListAdapter<LineItem, BasketListAdapter.CustomViewHolder>(DiffCallBack()) {

    private var itemClick: ((product: LineItem) -> Unit)? = null
    private var itemIncrease: ((counter: LineItem, position: Int) -> Unit)? = null
    private var itemDecrease: ((counter: LineItem, position: Int) -> Unit)? = null

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
        fun bind(item: LineItem) = binding.apply {
            val dec = DecimalFormat("###,###")
            val price = dec.format(item.price)
            productTitle.text = item.name
            counter.text = item.quantity.toString()
            productPrice.text = " $price  تومان "
            Glide.with(root)
                .load(item.metaData[0].value)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(productImage)

            if (counter.text == "1") {
                minus.setIconResource(R.drawable.ic_baseline_delete)
            } else minus.setIconResource(R.drawable.ic_baseline_remove)
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

    fun onItemPosition(clickListener: (LineItem) -> Unit) {
        itemClick = clickListener
    }

    fun onItemIncrease(clickListener: (LineItem, Int) -> Unit) {
        itemIncrease = clickListener
    }

    fun onItemDecrease(clickListener: (LineItem, Int) -> Unit) {
        itemDecrease = clickListener
    }

    class DiffCallBack : DiffUtil.ItemCallback<LineItem>() {
        override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem) =
            oldItem == newItem
    }
}

