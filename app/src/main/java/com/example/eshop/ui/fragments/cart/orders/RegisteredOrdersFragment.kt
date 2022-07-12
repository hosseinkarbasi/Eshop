package com.example.eshop.ui.fragments.cart.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.databinding.FragmentRegisteredOrdersBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.snackBar
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisteredOrdersFragment : Fragment(R.layout.fragment_registered_orders) {

    private var _binding: FragmentRegisteredOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisteredOrdersViewModel>()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredOrdersBinding.bind(view)

        initRecyclerView()
        getOrders()
    }

    private fun initRecyclerView() {
        ordersAdapter = OrdersAdapter()
        binding.ordersRv.adapter = ordersAdapter
    }

    private fun getOrders() = binding.apply {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getOrders(it.id, "completed")
            getOrdersList()
        }
    }

    private fun getOrdersList() {
        viewModel.getOrders.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    isError()
                }
            }
        }
    }

    private fun isSuccess(data: List<Order>) = binding.apply {
        loading.gone()
        loading.pauseAnimation()
        if (data.isEmpty()) {
            emptyOrders.visible()
            noOrdersTv.visible()
            ordersRv.gone()
        } else {
            ordersRv.visible()
            emptyOrders.gone()
            noOrdersTv.gone()
            ordersAdapter.submitList(data)
        }
    }

    private fun isError() = binding.apply {
        ordersRv.gone()
        emptyOrders.gone()
        noOrdersTv.gone()
        loading.gone()
        loading.pauseAnimation()
        requireView().snackBar("دریافت اطلاعات با مشکل مواجه شد")
    }

    private fun isLoading() = binding.apply {
        binding.ordersRv.gone()
        binding.emptyOrders.gone()
        binding.noOrdersTv.gone()
        binding.loading.visible()
        binding.loading.playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.ordersRv.adapter = null
        _binding = null
    }

}