package com.example.eshop.ui.fragments.basket

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.application.Constants.CUSTOMER_ID
import com.example.eshop.data.remote.model.Order
import com.example.eshop.databinding.FragmentBasketBinding
import com.example.eshop.utils.Mapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.data.remote.ResultWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.fragment_basket) {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BasketViewModel>()
    private lateinit var basketAdapter: BasketListAdapter
    private val orders = mutableListOf<LocalProduct>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBasketBinding.bind(view)

        iniRecyclerView()
        getProducts()
        setOrder()

    }


    private fun setOrder() {
        binding.cartSubmitBtn.setOnClickListener {
            val items = Mapper.transformProductsToLineItem(orders, 1)
            val order = Order(CUSTOMER_ID, items, "")
            viewModel.setOrder(order)
            getOrderStatus()
        }
    }

    private fun getOrderStatus() {
        viewModel.getOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    val orderDialog = MaterialAlertDialogBuilder(requireContext())
                    orderDialog.apply {
                        setTitle("ثبت سفارش")
                        setMessage(" سفارش شما با شماره پیگیری ${it.data?.number}ثبت شد. ")
                        setPositiveButton("ok") { _, _ ->
                        }
                        show()
                    }
                    viewModel.deleteAllProductsBasket()
                }
                is ResultWrapper.Error -> {
                    val orderDialog = MaterialAlertDialogBuilder(requireContext())
                    orderDialog.apply {
                        setTitle("ثبت سفارش")
                        setMessage(" متاسفانه ثبت سفارش شما با خطا مواجه شد ")
                        setPositiveButton("ok") { _, _ ->
                        }
                        show()
                    }
                }
            }
        }
    }

    private fun getProducts() {
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            basketAdapter.submitList(it)
            orders.clear()
            orders.addAll(it)
        }
    }

    private fun iniRecyclerView() {
        basketAdapter = BasketListAdapter()
        binding.basketRv.adapter = basketAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.basketRv.adapter = null
        _binding = null
    }
}