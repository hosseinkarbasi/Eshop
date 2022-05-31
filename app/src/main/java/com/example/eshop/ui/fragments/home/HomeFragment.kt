package com.example.eshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.remote.Constants.DATE
import com.example.eshop.data.remote.Constants.POPULARITY
import com.example.eshop.data.remote.Constants.RATING
import com.example.eshop.databinding.FragmentHomeBinding
import com.example.eshop.utils.Result
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
        seeMoreItems()
        searchProducts()
    }


    private fun seeMoreItems() = binding.apply {

        newestListBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductsListFragment(-1, DATE)
            findNavController().navigate(action)
        }
        mostSalesListBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductsListFragment(-1, RATING)
            findNavController().navigate(action)

        }
        mostViewedListBtn.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToProductsListFragment(-1, POPULARITY)
            findNavController().navigate(action)
        }
    }

    private fun getProductsList() {
        viewModel.getNewestProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    newestProductAdapter.submitList(it.data)
                }
            }
        }

        viewModel.getMostSalesProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            mostSalesProductAdapter.submitList(it.data)
        }
        viewModel.getMostViewedProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            mostViewedProductAdapter.submitList(it.data)
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

    private fun searchProducts() {
        binding.searchProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val search = query?.lowercase(Locale.getDefault())
                if (search.isNullOrEmpty()) return false
                else {
                    val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(query)
                    findNavController().navigate(action)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mostViewedRv.adapter = null
        binding.mostSalesRv.adapter = null
        binding.newestRv.adapter = null
        _binding = null
    }

}