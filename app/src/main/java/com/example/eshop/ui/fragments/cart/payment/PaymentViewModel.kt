package com.example.eshop.ui.fragments.cart.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Coupon
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.remote.model.SetOrder
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

    private val _getTotalPrice = MutableStateFlow(0)
    val getTotalPrice = _getTotalPrice.asStateFlow()

    private val _getOrder = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getOrder = _getOrder.asStateFlow()

    private val _getListOrder = MutableStateFlow<ResultWrapper<List<Order>>>(ResultWrapper.Loading)
    val getListOrder = _getListOrder.asStateFlow()

    private val _getProducts = MutableStateFlow<ResultWrapper<List<Product>>>(ResultWrapper.Loading)
    val getProducts = _getProducts.asStateFlow()

    private val _getCoupon = MutableStateFlow<ResultWrapper<List<Coupon>>>(ResultWrapper.Loading)
    val getCoupon = _getCoupon.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun updateOrder(orderId: Int, order: SetOrder) {
        viewModelScope.launch {
            cartRepository.updateOrder(orderId, order).collect {
                _getOrder.emit(it)
            }
        }
    }


    fun getProductsById(ids: Array<Int>) {
        viewModelScope.launch {
            cartRepository.getProductsList(ids).collect {
                _getProducts.emit(it)
            }
        }
    }

    fun getCartProduct(userId: Int, status: String) {
        viewModelScope.launch {
            cartRepository.getOrders(userId, status).collect {
                _getListOrder.emit(it)
            }
        }
    }

    fun setOrder(order: SetOrder) {
        viewModelScope.launch {
            cartRepository.setOrder(order).collect {
                _getOrder.emit(it)
            }
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