package com.example.eshop.ui.fragments.cart.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredOrdersViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userInfoDataStore: UserInfoDataStore
) :
    ViewModel() {

    private val _getOrders: MutableStateFlow<ResultWrapper<List<Order>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getOrders = _getOrders.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun getOrders(customerId: Int) {
        viewModelScope.launch {
            cartRepository.getOrders(customerId).collect {
                _getOrders.emit(it)
            }
        }
    }
}