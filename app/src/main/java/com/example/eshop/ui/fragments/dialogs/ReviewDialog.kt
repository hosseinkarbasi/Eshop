package com.example.eshop.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eshop.R
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.FragmentReviewDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewDialog(val productId: Int, private val reviewItem: (Review) -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentReviewDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewDialogBinding.inflate(inflater, container, false)

        binding.submitReviewBtn.setOnClickListener {

            if (!validateName() && !validateReview()) {
                return@setOnClickListener
            } else {
                with(binding) {
                    val review = Review(
                        0,
                        "",
                        productId,
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