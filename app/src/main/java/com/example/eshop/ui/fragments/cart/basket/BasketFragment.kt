package com.example.eshop.ui.fragments.cart.basket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.local.model.LocalProduct
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBasketBinding.bind(view)

        iniRecyclerView()
        viewModel.getLocalProducts()
        getProducts()
        changeQuantityCart()
        goProductInfo()
        totalPrice()
        goToPayment()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeQuantityCart() {
        basketAdapter.onItemIncrease { product, position ->
            viewModel.increase(product)
            this.basketAdapter.notifyDataSetChanged()
        }

        basketAdapter.onItemDecrease { product, position ->
            viewModel.decrease(product)
            this.basketAdapter.notifyDataSetChanged()
        }
    }

    private fun changeVisibility(list: List<LocalProduct>) = binding.apply {
        if (list.isEmpty()) {
            gpEmpty.visible()
            gpBasket.gone()

        } else {
            gpEmpty.gone()
            gpBasket.visible()
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

    private fun totalPrice() = binding.apply {
        viewModel.getTotalPrice.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            totalPrice.text = it.toString()
            totalPricePayment.text = it.toString()
            sumOfPrice.text = it.toString()
        }
    }

    private fun getProducts() = binding.apply {
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            basketAdapter.submitList(it)
            changeVisibility(it)
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