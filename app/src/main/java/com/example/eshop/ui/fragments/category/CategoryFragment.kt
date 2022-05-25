package com.example.eshop.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.databinding.FragmentCategoryBinding
import com.example.eshop.util.Result
import com.example.eshop.util.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryViewModel>()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        initRecyclerView()
        getCategoriesList()

    }

    private fun getCategoriesList() {
        viewModel.getClothingCategory.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    categoryAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.clothingRv.adapter = categoryAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.clothingRv.adapter = null
        _binding = null
    }
}