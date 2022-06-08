package com.example.eshop.ui.fragments.cart.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Coupon
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    userInfoDataStore: UserInfoDataStore
) : ViewModel() {

    init {
        getLocalProducts()
    }

    private val _getTotalPrice = MutableStateFlow(0)
    val getTotalPrice = _getTotalPrice.asStateFlow()

    private val _getOrder = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getOrder = _getOrder.asStateFlow()

    private val _getProducts = MutableStateFlow<List<LocalProduct>>(emptyList())
    val getProducts = _getProducts.asStateFlow()

    private val _getCoupon = MutableStateFlow<ResultWrapper<List<Coupon>>>(ResultWrapper.Loading)
    val getCoupon = _getCoupon.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun setOrder(order: Order) {
        viewModelScope.launch {
            cartRepository.setOrder(order).collect {
                _getOrder.emit(it)
            }
        }
    }

    private fun getLocalProducts() {
        viewModelScope.launch {
            cartRepository.getLocalProducts().collect { listProducts ->
                _getProducts.emit(listProducts)

                val total = listProducts.sumOf { it.price.toInt() * it.quantity }
                _getTotalPrice.emit(total)
            }
        }
    }

    fun deleteAllProductsBasket() {
        viewModelScope.launch {
            cartRepository.deleteAllProductsBasket()
        }
    }

    fun getCoupon(code: String) {
        viewModelScope.launch {
            cartRepository.getCoupon(code).collect {
                _getCoupon.emit(it)
            }
        }
    }

}