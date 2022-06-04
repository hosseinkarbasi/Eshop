package com.example.eshop.ui.fragments.productslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.POPULARITY
import com.example.eshop.application.Constants.RATING
import com.example.eshop.data.remote.model.Product
import com.example.eshop.databinding.FragmentProductsListBinding
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment : Fragment(R.layout.fragment_products_list) {

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsListViewModel>()
    private lateinit var productsAdapter: ProductsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsListBinding.bind(view)

        initRecyclerView()
        getProductsListFromCategory()
        goProductDetails()
        getProductsListFromHome()

    }

    private fun goProductDetails() {
        productsAdapter.onItemPosition {
            val action = ProductsListFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private fun getProductsListFromCategory() {
        val categoryId = viewModel.categoryId as Int
        viewModel.getProductsByCategory(categoryId)
        viewModel.getProductsByCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    it.message?.let { it1 -> isError(it1) }
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Success -> {
                    it.data?.let { it1 -> isSuccess(it1) }
                }
            }
        }
    }

    private fun getProductsListFromHome() {
        when (viewModel.productsType) {
            DATE -> {
                viewModel.getNewest()
                viewModel.getNewestProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                    when (it) {
                        is ResultWrapper.Error -> {
                            it.message?.let { it1 -> isError(it1) }
                        }
                        is ResultWrapper.Loading -> {
                            isLoading()
                        }
                        is ResultWrapper.Success -> {
                            it.data?.let { it1 -> isSuccess(it1) }
                        }
                    }
                }
            }
            RATING -> {
                viewModel.getMostSales()
                viewModel.getMostSalesProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                    when (it) {
                        is ResultWrapper.Error -> {
                            it.message?.let { it1 -> isError(it1) }
                        }
                        is ResultWrapper.Loading -> {
                            isLoading()
                        }
                        is ResultWrapper.Success -> {
                            it.data?.let { it1 -> isSuccess(it1) }
                        }
                    }
                }
            }
            POPULARITY -> {
                viewModel.getMostViewed()
                viewModel.getMostViewedProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                    when (it) {
                        is ResultWrapper.Error -> {
                            it.message?.let { it1 -> isError(it1) }
                        }
                        is ResultWrapper.Loading -> {
                            isLoading()
                        }
                        is ResultWrapper.Success -> {
                            it.data?.let { it1 -> isSuccess(it1) }
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        productsAdapter = ProductsListAdapter()
        binding.productsRv.adapter = productsAdapter
    }

    private fun isLoading() = binding.apply {
        loading.visible()
        loading.playAnimation()
    }

    private fun isError(errorMessage: String) = binding.apply {
        loading.visible()
        loading.playAnimation()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun isSuccess(data: List<Product>) = binding.apply {
        loading.gone()
        loading.pauseAnimation()
        productsAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productsRv.adapter = null
        _binding = null
    }
}