package com.example.eshop.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.POPULARITY
import com.example.eshop.application.Constants.RATING
import com.example.eshop.application.Constants.SPECIAL_SALE
import com.example.eshop.databinding.FragmentHomeBinding
import com.example.eshop.ui.fragments.product.ImageViewPagerAdapter
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var handler: Handler
    private lateinit var newestProductAdapter: ProductAdapter
    private lateinit var mostViewedProductAdapter: ProductAdapter
    private lateinit var mostSalesProductAdapter: ProductAdapter
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setUpViewPager()
        initRecyclerView()
        getSpecialSaleProducts()
        getProductsList()
        goProductDetails()
        seeMoreItems()
    }

    private fun getSpecialSaleProducts() {
        viewModel.getProduct(SPECIAL_SALE)
        viewModel.getSpecialSale.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            imageViewPagerAdapter.submitList(it.data?.images)
        }
    }

    private fun setUpViewPager() = binding.apply {
        handler = Handler(Looper.myLooper()!!)
        imageViewPagerAdapter = ImageViewPagerAdapter()
        sliderVp.adapter = imageViewPagerAdapter
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
                is ResultWrapper.Error -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mostViewedRv.adapter = null
        binding.mostSalesRv.adapter = null
        binding.newestRv.adapter = null
        binding.sliderVp.adapter = null
        _binding = null
    }
}