package com.example.eshop.ui.fragments.cart.payment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.databinding.FragmentPaymentBinding
import com.example.eshop.utils.Mapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaymentBinding.bind(view)

        getProducts()

    }

    private fun getProducts() {
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            setOrder(it)
        }
    }

    private fun setOrder(list: List<LocalProduct>) {
        binding.sumbit.setOnClickListener {
            viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) { userInfo ->
                val productsList = Mapper.transformProductsToLineItem(list)
                val order = Order(userInfo.id, productsList, "")
                viewModel.setOrder(order)
                getOrderStatus()
            }
        }
    }

    private fun getOrderStatus() {
        viewModel.getOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    val orderDialog = MaterialAlertDialogBuilder(requireContext())
                    orderDialog.apply {
                        setTitle("ثبت سفارش")
                        setMessage(" سفارش شما با شماره پیگیری ${it.data.number}ثبت شد. ")
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
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}