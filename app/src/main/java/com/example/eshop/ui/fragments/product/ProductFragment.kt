package com.example.eshop.ui.fragments.product

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.eshop.R
import com.example.eshop.databinding.FragmentProductBinding
import com.example.eshop.utils.Result
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductViewModel>()
    private val args by navArgs<ProductFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        getProduct()
    }

    private fun getProduct() = binding.apply {
        viewModel.getProduct(args.productId)
        viewModel.getProduct.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    productDetails = it.data
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}