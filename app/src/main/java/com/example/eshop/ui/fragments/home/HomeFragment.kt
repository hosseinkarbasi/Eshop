package com.example.eshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.databinding.FragmentHomeBinding
import com.example.eshop.util.Result
import com.example.eshop.util.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        getProductsList()

    }


    private fun getProductsList() {

        viewModel.getProduct.collectWithRepeatOnLifecycle(viewLifecycleOwner) {

            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    productAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter()
        binding.mostViewedRv.adapter = productAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mostViewedRv.adapter = null
        _binding = null
    }
}