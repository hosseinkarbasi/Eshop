package com.example.eshop.ui.fragments.cart.basket

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.*
import com.example.eshop.databinding.FragmentBasketBinding
import com.example.eshop.ui.fragments.cart.HomeCartFragmentDirections
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.fragment_basket) {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BasketViewModel>()
    private lateinit var basketAdapter: BasketListAdapter
    var customerId: Int = 0
    var orderId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBasketBinding.bind(view)

        iniRecyclerView()
        getOrder()
        goProductInfo()
        inc()
        dec()
        goToPayment()
        updateOrder()
    }

    private fun updateOrder() {
        viewModel.getUpdateOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {}
                is ResultWrapper.Success -> {
                    basketAdapter.submitList(it.data.lineItems)
                }
            }
        }
    }

    private fun inc() {
        basketAdapter.onItemIncrease { item, _ ->
            val x = item.quantity
            val lineItemList = mutableListOf<LineItem>()
            val lineItem = LineItem(
                item.id,
                item.name,
                item.productId,
                x + 1,
                emptyList(),
                0
            )

            lineItemList.add(lineItem)
            val order = SetOrder(
                orderId,
                customerId,
                lineItemList,
                "pending",
                emptyList()
            )
            viewModel.updateOrder(orderId, order)
        }
    }

    private fun dec() {
        basketAdapter.onItemDecrease { item, _ ->

            val x = item.quantity
            val lineItemList = mutableListOf<LineItem>()
            val lineItem = LineItem(
                item.id,
                item.name,
                item.productId,
                x - 1,
                emptyList(),
                0
            )

            lineItemList.add(lineItem)
            val order = SetOrder(
                orderId,
                customerId,
                lineItemList,
                "pending",
                emptyList()
            )
            viewModel.updateOrder(orderId, order)

            if (item.quantity <= 0) {
                deleteProductFromOrder(item)
            } else {
                viewModel.updateOrder(orderId, order)
            }
        }
    }

    private fun deleteProductFromOrder(Item: LineItem) {
        val lineItemList = mutableListOf<LineItem>()
        val lineItem = LineItem(
            Item.id,
            "",
            Item.productId,
            0,
            emptyList(),
            0
        )

        lineItemList.add(lineItem)
        val order = SetOrder(
            orderId,
            customerId,
            lineItemList,
            "pending",
            emptyList()
        )
        viewModel.updateOrder(orderId, order)
    }

    private fun getOrder() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getCartProduct(it.id, "pending")
            customerId = it.id
        }
        viewModel.getOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    isError(it.message.toString())
                }
                is ResultWrapper.Success -> {
                    isSuccess()
                    if (it.data.isNotEmpty()) {
                        orderId = it.data[0].id
                        totalPrice(it.data[0])
                        basketAdapter.submitList(it.data[0].lineItems)
                        changeVisibility(it.data[0].lineItems)
                    } else {
                        binding.loading.gone()
                        binding.gpBasket.gone()
                        binding.gpEmpty.visible()
                    }
                }
            }
        }
    }

    private fun goProductInfo() {
        basketAdapter.onItemPosition {
            val action = BasketFragmentDirections.actionGlobalProductFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private fun goToPayment() {
        binding.cartSubmitBtn.setOnClickListener {
            viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                val checkUserLogged = viewModel.pref.first()
                if (checkUserLogged.email.isNotEmpty()) {
                    val action =
                        HomeCartFragmentDirections.actionHomeCartFragmentToPaymentFragment()
                    findNavController().navigate(action)
                } else {
                    val action =
                        HomeCartFragmentDirections.actionHomeCartFragmentToLoginDialog()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun changeVisibility(list: List<LineItem>) = binding.apply {
        if (list.isEmpty()) {
            gpEmpty.visible()
            gpBasket.gone()
        } else {
            gpEmpty.gone()
            gpBasket.visible()
        }
    }

    private fun totalPrice(data: Order) = binding.apply {
        totalPrice.text = data.total
        totalPricePayment.text = data.total
        sumOfPrice.text = data.total
    }

    private fun iniRecyclerView() {
        basketAdapter = BasketListAdapter()
        binding.basketRv.adapter = basketAdapter
    }

    private fun isLoading() = binding.apply {
        loading.visible()
        gpEmpty.gone()
        gpBasket.gone()
        loading.playAnimation()
    }

    private fun isError(errorMessage: String) = binding.apply {
        loading.gone()
        gpEmpty.visible()
        gpBasket.gone()
        loading.pauseAnimation()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun isSuccess() = binding.apply {
        loading.gone()
        gpEmpty.gone()
        gpBasket.visible()
        loading.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.basketRv.adapter = null
        _binding = null
    }
}