package com.example.eshop.ui.fragments.reviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.ReviewsListItemBinding
import java.text.DateFormat

class ReviewsAdapter() : RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    private var itemClick: ((product: Review) -> Unit)? = null
    var list = mutableListOf<Review>()

    inner class ReviewsViewHolder(val binding: ReviewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) = binding.apply {
            reviewerTv.text = item.reviewer
            ratingTv.text = item.rating.toString()
            reviewTv.text = HtmlCompat.fromHtml(item.review, HtmlCompat.FROM_HTML_MODE_LEGACY)
            dateReviewerTv.text = item.date_created
            binding.root.setOnClickListener {
                itemClick?.let {
                    it(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder =
        ReviewsViewHolder(
            ReviewsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun onItemPosition(clickListener: (Review) -> Unit) {
        itemClick = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(items: List<Review>?) {
        if (items != null) {
            list.clear()
            list.addAll(items)
        }
        notifyDataSetChanged()
    }
}
