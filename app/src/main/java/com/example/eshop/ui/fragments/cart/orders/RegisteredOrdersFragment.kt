package com.example.eshop.ui.fragments.cart.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentRegisteredOrdersBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisteredOrdersFragment : Fragment(R.layout.fragment_registered_orders) {

    private var _binding: FragmentRegisteredOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisteredOrdersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredOrdersBinding.bind(view)

    }

    private fun getOrders() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getOrders(it.id)
        }

        viewModel.getOrders.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}