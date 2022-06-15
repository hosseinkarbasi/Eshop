package com.example.eshop.ui.fragments.cart.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.SetOrder
import com.example.eshop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    userInfoDataStore: UserInfoDataStore
) : ViewModel() {

    private val _getOrder = MutableStateFlow<ResultWrapper<List<Order>>>(ResultWrapper.Loading)
    val getOrder = _getOrder.asStateFlow()

    private val _getUpdateOrder =
        MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getUpdateOrder = _getUpdateOrder.asStateFlow()

    val pref = userInfoDataStore.preferences


    fun updateOrder(orderId: Int, order: SetOrder) {
        viewModelScope.launch {
            cartRepository.updateOrder(orderId, order).collect {
                _getUpdateOrder.emit(it)
            }
        }
    }

    fun getCartProduct(userId: Int, status: String) {
        viewModelScope.launch {
            cartRepository.getOrders(userId, status).collect {
                _getOrder.emit(it)
            }
        }
    }

}