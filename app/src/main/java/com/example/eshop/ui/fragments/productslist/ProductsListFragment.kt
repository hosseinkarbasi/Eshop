package com.example.eshop.ui.fragments.productslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var adapter: ProductsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsListBinding.bind(view)
        layoutManager = LinearLayoutManager(activity)

        initRecyclerView()
        goProductDetails()
        getCompleteProducts()
        pagination()
        getProductsList()

    }

    private fun getCompleteProducts() {
        when (val productsType = viewModel.productsType) {
            DATE -> {
                viewModel.getNewest(5, page)
            }
            RATING -> {
                viewModel.getMostSales(5, page)
            }
            POPULARITY -> {
                viewModel.getMostViewed(5, page)
            }
            else -> {
                viewModel.getProductsByCategory(productsType.toInt(), 5, page)
            }
        }
    }

    private fun getProductsList() {
        viewModel.getProductsList.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    it.message?.let { it1 -> isError(it1) }
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                }
            }
        }
    }

    private fun pagination() {
        binding.productsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = binding.productsRv.adapter?.itemCount
                if (page >= 1) {
                    if (visibleItemCount + pastVisibleItem >= total!!) {
                        page++
                        getCompleteProducts()
                    }
                }
            }
        })
    }

    private fun goProductDetails() {
        adapter.onItemPosition {
            val action = ProductsListFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() {
        binding.productsRv.layoutManager = layoutManager
        adapter = ProductsAdapter()
        binding.productsRv.adapter = adapter
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
        adapter.addList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productsRv.adapter = null
        _binding = null
    }
}