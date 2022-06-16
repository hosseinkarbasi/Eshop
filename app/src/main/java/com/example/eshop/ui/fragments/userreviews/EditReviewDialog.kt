package com.example.eshop.ui.fragments.userreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.eshop.R
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.FragmentReviewDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditReviewDialog(val review: Review, private val reviewItem: (Review) -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentReviewDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewDialogBinding.inflate(inflater, container, false)

        binding.reviewEd.setText(
            HtmlCompat.fromHtml(
                review.review,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.ratingBar.rating = review.rating.toFloat()
        binding.reviewerEmailEd.setText(review.reviewerEmail)
        binding.nameReviewerEd.setText(review.reviewer)
        binding.submitReviewBtn.text = "ویرایش دیدگاه"

        binding.submitReviewBtn.setOnClickListener {

            if (!validateName() && !validateReview()) {
                return@setOnClickListener
            } else {
                with(binding) {
                    val review = Review(
                        review.id,
                        review.dateCreated,
                        review.productId,
                        ratingBar.rating.toInt(),
                        reviewEd.text.toString(),
                        nameReviewerEd.text.toString(),
                        reviewerEmailEd.text.toString()
                    )
                    reviewItem(review)
                    dismiss()
                }
            }
        }

        binding.btnNegative.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun validateName(): Boolean {
        return if (binding.nameReviewerEd.text?.isEmpty() == true) {
            binding.nameReviewerLayout.helperText = "ایمیل را وارد کنید"
            false
        } else {
            binding.nameReviewerLayout.helperText = null
            true
        }
    }

    private fun validateReview(): Boolean {
        return if (binding.reviewEd.text?.isEmpty() == true) {
            binding.reviewLayout.helperText = "نظر خود را بنویسید"
            false
        } else {
            binding.reviewLayout.helperText = null
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}