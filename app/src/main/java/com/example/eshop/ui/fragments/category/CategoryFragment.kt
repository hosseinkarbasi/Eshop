package com.example.eshop.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.databinding.FragmentCategoryBinding
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
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

    private fun goProductsByCategory() {
        clothingAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id,"")
            findNavController().navigate(action)
        }

        digitalAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id,"")
            findNavController().navigate(action)
        }

        superMarketAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id,"")
            findNavController().navigate(action)
        }

        booksAndArtAdapter.onItemPosition {
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductsListFragment(it.id,"")
            findNavController().navigate(action)
        }
    }

    private fun getCategoriesList() {
        viewModel.getClothingCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    clothingAdapter.submitList(it.data)
                }
            }
        }
        viewModel.getDigitalCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    digitalAdapter.submitList(it.data)
                }
            }
        }
        viewModel.getSuperMarketCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    superMarketAdapter.submitList(it.data)
                }
            }
        }
        viewModel.getBooksAndArtCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    booksAndArtAdapter.submitList(it.data)
                }
            }
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