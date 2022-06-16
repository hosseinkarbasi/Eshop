package com.example.eshop.ui.fragments.userreviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eshop.data.remote.model.LineItem
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.UserReviewsListItemBinding

class UserReviewsAdapter : RecyclerView.Adapter<UserReviewsAdapter.ReviewsViewHolder>() {

    private var itemClick: ((review: Review) -> Unit)? = null
    private var itemDelete: ((item: Review, position: Int) -> Unit)? = null
    private var itemEdit: ((item: Review, position: Int) -> Unit)? = null
    var list = mutableListOf<Review>()

    inner class ReviewsViewHolder(val binding: UserReviewsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) = binding.apply {
            reviewerTv.text = item.reviewer
            ratingTv.text = item.rating.toString()
            reviewTv.text = HtmlCompat.fromHtml(item.review, HtmlCompat.FROM_HTML_MODE_LEGACY)
            dateReviewerTv.text = item.dateCreated

            root.setOnClickListener {
                itemClick?.let {
                    it(item)
                }
            }

            deleteReviewBtn.setOnClickListener {
                itemDelete?.let {
                    it(item, bindingAdapterPosition)
                }
            }

            editReviewBtn.setOnClickListener {
                itemEdit?.let {
                    it(item, bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder =
        ReviewsViewHolder(
            UserReviewsListItemBinding.inflate(
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

    fun itemDelete(clickListener: (Review, Int) -> Unit) {
        itemDelete = clickListener
    }

    fun itemEdit(clickListener: (Review, Int) -> Unit) {
        itemEdit = clickListener
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
