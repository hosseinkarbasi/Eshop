package com.example.eshop.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.databinding.FragmentCategoryBinding
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Category
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.snackBar
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryViewModel>()
    private lateinit var clothingAdapter: CategoryAdapter
    private lateinit var digitalAdapter: CategoryAdapter
    private lateinit var superMarketAdapter: CategoryAdapter
    private lateinit var booksAndArtAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        initRecyclerView()
        getCategoriesList()
        goProductsByCategory()

    }

    private fun getCategoriesList() {
        viewModel.getClothingCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(clothingAdapter, it)
        }

        viewModel.getDigitalCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(digitalAdapter, it)
        }

        viewModel.getSuperMarketCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(superMarketAdapter, it)
        }

        viewModel.getBooksAndArtCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setResultRequest(booksAndArtAdapter, it)
        }
    }

    private fun setResultRequest(adapter: CategoryAdapter, result: ResultWrapper<List<Category>>) {
        when (result) {
            is ResultWrapper.Success -> success(adapter, result.data)
            is ResultWrapper.Error -> error()
            is ResultWrapper.Loading -> loading()
        }
    }

    private fun success(adapter: CategoryAdapter, list: List<Category>) = binding.apply {
        adapter.submitList(list)
        if (viewModel.isSuccess()) {
            scr.visible()
            loading.gone()
            loading.pauseAnimation()
            retry.gone()
        }
    }

    private fun error() = binding.apply {
        if (viewModel.isError()) {
            scr.gone()
            loading.gone()
            loading.pauseAnimation()
            retry.visible()
            retry.setOnClickListener {
                viewModel.retry()
            }
            requireView().snackBar("دریافت اطلاعات با مشکل مواجه شد")
        }
    }

    private fun loading() = binding.apply {
        loading.visible()
        scr.gone()
        retry.gone()
        loading.playAnimation()
    }

    private fun goProductsByCategory() {
        clothingAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id.toString())
            findNavController().navigate(action)
        }

        digitalAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id.toString())
            findNavController().navigate(action)
        }

        superMarketAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id.toString())
            findNavController().navigate(action)
        }

        booksAndArtAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id.toString())
            findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() = binding.apply {
        clothingAdapter = CategoryAdapter()
        digitalAdapter = CategoryAdapter()
        superMarketAdapter = CategoryAdapter()
        booksAndArtAdapter = CategoryAdapter()

        clothingRv.adapter = clothingAdapter
        digitalRv.adapter = digitalAdapter
        superMarketRv.adapter = superMarketAdapter
        booksAndArtRv.adapter = booksAndArtAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.clothingRv.adapter = null
        binding.digitalRv.adapter = null
        binding.superMarketRv.adapter = null
        binding.booksAndArtRv.adapter = null
        _binding = null
    }
}