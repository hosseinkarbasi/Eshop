package com.example.eshop.ui.fragments.reviews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.FragmentReviewsBinding
import com.example.eshop.ui.fragments.dialogs.ReviewDialog
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : Fragment(R.layout.fragment_reviews) {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReviewsViewModel>()
    private lateinit var adapter: ReviewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewsBinding.bind(view)

        initRecyclerView()
        getReviews()
        submitReview()
        review()
    }

    private fun review(){
        viewModel.getReview.collectWithRepeatOnLifecycle(viewLifecycleOwner){
            when(it){
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    Toast.makeText(requireContext(), "نظر شما یا موفقیت ثبت شد", Toast.LENGTH_LONG).show()
                    viewModel.getReviews(viewModel.productId, 1, 100)
                }
                is ResultWrapper.Error -> {
                    Toast.makeText(requireContext(), "مشکلی در ثبت نظر بوجود آمد", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun submitReview() {
        binding.reviewFab.setOnClickListener {
            val dialog = ReviewDialog(viewModel.productId) {
                viewModel.createReview(it)
            }
            dialog.show(childFragmentManager, "review")
        }
    }

    private fun getReviews() {
        viewModel.getReviews(viewModel.productId, 1, 100)
        viewModel.getReviewsList.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    isError()
                }
            }
        }
    }

    private fun isSuccess(data: List<Review>) = binding.apply {
        adapter.addList(data)
        reviewsRv.visible()
        loading.gone()
        loading.pauseAnimation()
    }

    private fun isError() = binding.apply {
        reviewsRv.gone()
        loading.gone()
        loading.pauseAnimation()
        Toast.makeText(requireContext(), "دریافت اطلاعات با مشکل مواجه شد", Toast.LENGTH_LONG)
            .show()
    }

    private fun isLoading() = binding.apply {
        reviewsRv.gone()
        loading.visible()
        loading.playAnimation()
    }

    private fun initRecyclerView() {
        adapter = ReviewsAdapter()
        binding.reviewsRv.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.reviewsRv.adapter = null
        _binding = null
    }
}