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


    }

    private fun getReviews() {
        viewModel.getReviews(viewModel.productId as Int, 1, 100)
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