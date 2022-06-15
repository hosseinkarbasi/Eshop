package com.example.eshop.ui.fragments.userreviews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Review
import com.example.eshop.databinding.FragmentUserReviewsBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserReviewsFragment : Fragment(R.layout.fragment_user_reviews) {

    private var _binding: FragmentUserReviewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserReviewsViewModel>()
    private lateinit var adapter: UserReviewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserReviewsBinding.bind(view)

        initRecyclerView()
        getUserReviews()
    }

    private fun getUserReviews() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getUserReviews(it.email, 100, 1)
        }

        viewModel.getUserReviews.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    isError()
                }
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = UserReviewsAdapter()
        binding.userReviewsRv.adapter = adapter
    }

    private fun isSuccess(data: List<Review>) = binding.apply {
        adapter.addList(data)
        userReviewsRv.visible()
        loading.gone()
        loading.pauseAnimation()
    }

    private fun isError() = binding.apply {
        userReviewsRv.gone()
        loading.gone()
        loading.pauseAnimation()
        Toast.makeText(requireContext(), "دریافت اطلاعات با مشکل مواجه شد", Toast.LENGTH_LONG)
            .show()
    }

    private fun isLoading() = binding.apply {
        userReviewsRv.gone()
        loading.visible()
        loading.playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.userReviewsRv.adapter = null
        _binding = null
    }
}