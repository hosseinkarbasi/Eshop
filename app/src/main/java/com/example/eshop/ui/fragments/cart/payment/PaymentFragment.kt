package com.example.eshop.ui.fragments.cart.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.*
import com.example.eshop.databinding.FragmentPaymentBinding
import com.example.eshop.utils.Mapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PaymentViewModel>()
    private lateinit var adapter: PaymentAdapter
    var customerId: Int = 0
    private var couponLine = mutableListOf<Coupon>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaymentBinding.bind(view)

        initRecyclerView()
        getOrder()
        getOrderStatus()
        getCoupon()
    }

    private fun setOrder(orderId: List<Order>) {
        binding.finishSubmitBtn.setOnClickListener {
            val order = SetOrder(
                orderId[0].id,
                customerId,
                emptyList(),
                "completed",
                couponLine
            )
            viewModel.updateOrder(orderId[0].id, order)
        }
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
                    couponLine.clear()
                    couponEdLayout.helperText = "کد تخفیف وارد شده درست نیست"
                }
                is ResultWrapper.Success -> {
                    couponLine.addAll(it.data)
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

    private fun getOrder() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getCartProduct(it.id, "pending")
            customerId = it.id
        }
        viewModel.getListOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is ResultWrapper.Success -> {
                    setOrder(it.data)
                    getProducts(it.data[0].lineItems)
                    totalPrice(it.data[0])
                    viewModel.getProductsById(Mapper.transformLineItemToProductsId(it.data[0].lineItems))
                }
            }
        }
    }

    private fun getProducts(lineItem: List<LineItem>) {
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    isError(it.message.toString())
                }
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                    val product = Mapper.transformRemoteProductToLocalProduct(it.data, lineItem)
                    adapter.submitList(product)
                }
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun getProducts() {
//        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) { productList ->
//            setOrder(productList)
//            adapter.submitList(productList)
//            val totalQuantity = productList.sumOf { it.quantity }.toString()
//            binding.totalQuantity.text = "$totalQuantity کالا "
//        }
//    }

//    @SuppressLint("SetTextI18n")
//    private fun totalPrice() = binding.apply {
//        viewModel.getTotalPrice.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
//            val dec = DecimalFormat("###,###")
//            val price = dec.format(it)
//            binding.totalPrice.text = "$price تومان "
//            binding.totalPricePayment.text = "$price تومان "
//
//        }
//    }

//    private fun setOrder(list: List<LocalProduct>) {
//        binding.finishSubmitBtn.setOnClickListener {
//            viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) { userInfo ->
//                val productsList = Mapper.transformProductsToLineItem(list)
//                val order = Order(userInfo.id, productsList, "",)
//                viewModel.setOrder(order)
//                getOrderStatus()
//            }
//        }
//    }

    private fun getOrderStatus() {
        viewModel.getOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    val orderDialog =
                        MaterialAlertDialogBuilder(requireContext(), R.style.RoundShapeTheme)
                    orderDialog.apply {
                        setTitle("ثبت سفارش")
                        setMessage(" سفارش شما با شماره پیگیری ${it.data.number}ثبت شد. ")
                        setPositiveButton("ok") { _, _ ->
                            val action =
                                PaymentFragmentDirections.actionPaymentFragmentToHomeCartFragment()
                            findNavController().navigate(action)
                        }
                        show()
                    }
//                    viewModel.deleteAllProductsBasket()
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

    private fun totalPrice(data: Order) = binding.apply {
        val dec = DecimalFormat("###,###")
        val price = dec.format(data.total?.toInt())
        binding.totalPrice.text = "$price تومان "
        binding.totalPricePayment.text = "$price تومان "
        val totalQuantity = data.lineItems.sumOf { it.quantity }.toString()
        binding.totalQuantity.text = "$totalQuantity کالا "

    }

    private fun isLoading() = binding.apply {
        loading.visible()
        scrollView.gone()
        loading.playAnimation()
    }

    private fun isError(errorMessage: String) = binding.apply {
        loading.gone()
        scrollView.visible()
        loading.pauseAnimation()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun isSuccess(list: List<Product>) = binding.apply {
        loading.gone()
        scrollView.visible()
        loading.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productsRv.adapter = null
        _binding = null
    }

}