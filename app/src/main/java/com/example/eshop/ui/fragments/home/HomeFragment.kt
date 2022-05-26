package com.example.eshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.databinding.FragmentHomeBinding
import com.example.eshop.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var newestProductAdapter: ProductAdapter
    private lateinit var mostViewedProductAdapter: ProductAdapter
    private lateinit var mostSalesProductAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        getProductsList()
        goProductDetails()
    }


    private fun getProductsList() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getNewestProducts.collect {
                        when (it) {
                            is Result.Error -> {}
                            is Result.Loading -> {}
                            is Result.Success -> {
                                newestProductAdapter.submitList(it.data)
                            }
                        }
                    }
                }
                launch {
                    viewModel.getMostSalesProducts.collect {
                        mostSalesProductAdapter.submitList(it.data)
                    }
                }
                launch {
                    viewModel.getMostViewedProducts.collect {
                        mostViewedProductAdapter.submitList(it.data)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() = binding.apply {
        newestProductAdapter = ProductAdapter()
        mostViewedProductAdapter = ProductAdapter()
        mostSalesProductAdapter = ProductAdapter()
        mostViewedRv.adapter = mostViewedProductAdapter
        newestRv.adapter = newestProductAdapter
        mostSalesRv.adapter = mostSalesProductAdapter

    }

    private fun goProductDetails() {
        newestProductAdapter.onItemPosition {
            val action = HomeFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
        mostViewedProductAdapter.onItemPosition {
            val action = HomeFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
        mostSalesProductAdapter.onItemPosition {
            val action = HomeFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mostViewedRv.adapter = null
        binding.mostSalesRv.adapter = null
        binding.newestRv.adapter = null
        _binding = null
    }
}