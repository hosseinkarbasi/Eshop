package com.example.eshop.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.data.remote.model.ProductImage
import com.example.eshop.databinding.ImageItemBinding

class SliderImageAdapter(
    private val sliderItems: MutableList<ProductImage>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<SliderImageAdapter.SliderViewHolder>() {


    inner class SliderViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(position: Int) = binding.apply {
            Glide.with(binding.root)
                .load(sliderItems[position].src)
                .placeholder(R.drawable.online_shopping_palceholder)
                .into(binding.productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        SliderViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setData(position)
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount() = sliderItems.size

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }
}