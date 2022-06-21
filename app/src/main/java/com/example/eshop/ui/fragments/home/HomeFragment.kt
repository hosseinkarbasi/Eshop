package com.example.eshop.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.eshop.R
import com.example.eshop.application.Constants.DATE
import com.example.eshop.application.Constants.POPULARITY
import com.example.eshop.application.Constants.RATING
import com.example.eshop.application.Constants.SPECIAL_SALE
import com.example.eshop.databinding.FragmentHomeBinding
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.model.ProductImage
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private var imageList = mutableListOf<ProductImage>()
    lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private lateinit var newestProductAdapter: ProductAdapter
    private lateinit var mostViewedProductAdapter: ProductAdapter
    private lateinit var mostSalesProductAdapter: ProductAdapter
    private lateinit var imageViewPagerAdapter: SliderImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        getSpecialProducts()
        setUpViewPager()
        getProductsList()
        goProductDetails()
        seeMoreItems()
        goToSearch()
    }

    private fun setResultRequest(adapter: ProductAdapter, result: ResultWrapper<List<Product>>) {
        when (result) {
            is ResultWrapper.Success -> success(adapter, result.data)
            is ResultWrapper.Error -> error()
            is ResultWrapper.Loading -> loading()
        }
    }

    private fun getProductsList() {
        viewModel.getNewestProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(newestProductAdapter, it)
        }

        viewModel.getMostViewedProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(mostViewedProductAdapter, it)
        }

        viewModel.getMostSalesProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(mostSalesProductAdapter, it)
        }
    }

    private fun success(adapter: ProductAdapter, list: List<Product>) = binding.apply {
        adapter.submitList(list)
        if (viewModel.isSuccess()) {
            cardViewSearch.visible()
            scr.visible()
            loading.gone()
            loading.pauseAnimation()
            retry.gone()
        }
    }

    private fun error() = binding.apply {
        if (viewModel.isError()) {
            scr.gone()
            cardViewSearch.gone()
            loading.gone()
            loading.pauseAnimation()
            retry.visible()
            retry.setOnClickListener {
                viewModel.retry()
            }

            Toast.makeText(
                requireContext(),
                "دریافت اطلاعات با مشکل مواجه شد",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun loading() = binding.apply {
        loading.visible()
        cardViewSearch.gone()
        scr.gone()
        retry.gone()
        loading.playAnimation()
    }

    private fun goToSearch() {
        binding.searchBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    private fun getSpecialProducts() {
        viewModel.getSpecialProduct(SPECIAL_SALE)
        viewModel.getSpecialSale.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    imageList.addAll(it.data.images)
                    imageViewPagerAdapter = SliderImageAdapter(imageList, binding.sliderVp)
                    binding.sliderVp.adapter = imageViewPagerAdapter
                }
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {}
            }
        }
    }

    private fun setUpViewPager() = binding.apply {
        handler = Handler(Looper.myLooper()!!)

        sliderVp.clipToPadding = false
        sliderVp.clipChildren = false
        sliderVp.offscreenPageLimit = 3
        sliderVp.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(20))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1.0f - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        sliderVp.setPageTransformer(compositePageTransformer)

        runnable = Runnable {
            binding.sliderVp.currentItem = binding.sliderVp.currentItem + 1
        }

        sliderVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderVp.removeCallbacks(runnable)
                sliderVp.postDelayed(runnable, 3000)
            }
        })

    }

    private fun seeMoreItems() = binding.apply {

        newestListBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductsListFragment(DATE)
            findNavController().navigate(action)
        }
        mostSalesListBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProductsListFragment(RATING)
            findNavController().navigate(action)

        }
        mostViewedListBtn.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToProductsListFragment(POPULARITY)
            findNavController().navigate(action)
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

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mostViewedRv.adapter = null
        binding.mostSalesRv.adapter = null
        binding.newestRv.adapter = null
        binding.sliderVp.adapter = null
    }

}