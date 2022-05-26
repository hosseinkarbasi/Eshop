package com.example.eshop.ui.fragments.productslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eshop.R
import com.example.eshop.databinding.FragmentProductsListBinding
import com.example.eshop.utils.Result
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment : Fragment(R.layout.fragment_products_list) {

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsListViewModel>()
    private val args by navArgs<ProductsListFragmentArgs>()
    private lateinit var productsAdapter: ProductsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsListBinding.bind(view)

        initRecyclerView()
        getProductsList()
        goProductDetails()

    }

    private fun goProductDetails() {
        productsAdapter.onItemPosition {
            val action = ProductsListFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private fun getProductsList() {
        viewModel.getProductsByCategory(args.categoryId)
        viewModel.getProductsByCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    productsAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun initRecyclerView() {
        productsAdapter = ProductsListAdapter()
        binding.productsRv.adapter = productsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productsRv.adapter = null
        _binding = null
    }
}