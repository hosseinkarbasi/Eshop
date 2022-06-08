package com.example.eshop.ui.fragments.cart.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Coupon
import com.example.eshop.data.remote.model.Order
import com.example.eshop.databinding.FragmentPaymentBinding
import com.example.eshop.utils.Mapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PaymentViewModel>()
    private lateinit var adapter: PaymentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaymentBinding.bind(view)

        initRecyclerView()
        getProducts()
        totalPrice()
        getCoupon()
    }

    private fun getCoupon() = binding.apply {
        couponEdLayout.setEndIconOnClickListener {
            if (!validateEditText()) {
                return@setEndIconOnClickListener
            } else {
                viewModel.getCoupon(couponEd.text.toString())
            }
        }

        viewModel.getCoupon.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    couponEdLayout.helperText = "کد تخفیف وارد شده درست نیست"
                }
                is ResultWrapper.Success -> {
                    isSuccessCoupon(it.data)
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }

    private fun validateEditText(): Boolean {
        return if (binding.couponEd.text?.isEmpty() == true) {
            binding.couponEdLayout.helperText = "کد تخفیف را وارد کنید"
            false
        } else {
            binding.couponEdLayout.helperText = null
            true
        }
    }

    private fun isSuccessCoupon(data: List<Coupon>) {
        if (data.isEmpty()) {
            binding.couponEdLayout.helperText = "کد تخفیف وارد شده درست نیست"
        } else {
            viewModel.getTotalPrice.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                val totalPrice = it - it / data[0].amount.toDouble()
                val convertPercent = DecimalFormat("0.#")
                val percent = convertPercent.format(data[0].amount.toDouble())
                val dec = DecimalFormat("###,###")
                val price = dec.format(totalPrice)
                binding.couponEdLayout.helperText = "$percent درصد از مبلغ کل کسر شد "
                binding.couponPercent.text = "$percent درصد "
                binding.totalPricePayment.text = "$price تومان "
            }
        }
    }

    private fun initRecyclerView() {
        adapter = PaymentAdapter()
        binding.productsRv.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun getProducts() {
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) { productList ->
            setOrder(productList)
            adapter.submitList(productList)
            val totalQuantity = productList.sumOf { it.quantity }.toString()
            binding.totalQuantity.text = "$totalQuantity کالا "
        }
    }

    @SuppressLint("SetTextI18n")
    private fun totalPrice() = binding.apply {
        viewModel.getTotalPrice.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            val dec = DecimalFormat("###,###")
            val price = dec.format(it)
            binding.totalPrice.text = "$price تومان "
            binding.totalPricePayment.text = "$price تومان "

        }
    }

    private fun setOrder(list: List<LocalProduct>) {
        binding.finishSubmitBtn.setOnClickListener {
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
        binding.productsRv.adapter = null
        _binding = null
    }

}