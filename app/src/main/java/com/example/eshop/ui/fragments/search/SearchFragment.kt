package com.example.eshop.ui.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.application.Constants.ASC
import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.DESC
import com.example.eshop.application.Constants.PRICE
import com.example.eshop.application.Constants.RATING
import com.example.eshop.data.remote.model.Product
import com.example.eshop.databinding.FragmentSearchBinding
import com.example.eshop.ui.fragments.productslist.ProductsListAdapter
import com.example.eshop.ui.fragments.productslist.ProductsListFragmentDirections
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.ui.fragments.home.HomeFragmentDirections
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: ProductsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        initRecyclerView()
        resultSearch()
        goProductDetails()
        orderingSearchResult()
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

    private fun orderingSearchResult() = binding.apply {
        val searchQuery = viewModel.searchText
        sort.setOnClickListener {
            val dialog = OrderingDialog { orderingText ->
                when (orderingText) {
                    SortingText.PRICE_ASC -> {
                        viewModel.searchProducts(searchQuery, PRICE, ASC)
                    }
                    SortingText.PRICE_DESC -> {
                        viewModel.searchProducts(searchQuery, PRICE, DESC)
                    }
                    SortingText.DATE -> {
                        viewModel.searchProducts(searchQuery, DATE, DESC)
                    }
                    SortingText.RATING -> {
                        viewModel.searchProducts(searchQuery, RATING, DESC)
                    }
                }
                sort.text = orderingText.text
            }
            dialog.show(childFragmentManager, "sort")
        }
    }

    private fun initRecyclerView() {
        searchAdapter = ProductsListAdapter()
        binding.searchRv.adapter = searchAdapter
    }

    private fun resultSearch() {
        viewModel.searchProducts(viewModel.searchText, DATE, DESC)
        viewModel.getSearchText.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
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

    private fun goProductDetails() {
        searchAdapter.onItemPosition {
            val action = ProductsListFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
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
        searchAdapter.submitList(data)
    }

    private fun View.visible() {
        visibility = View.VISIBLE
    }

    private fun View.gone() {
        visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.searchRv.adapter = null
        _binding = null
    }
}