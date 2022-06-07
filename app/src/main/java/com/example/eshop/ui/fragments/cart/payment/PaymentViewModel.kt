package com.example.eshop.ui.fragments.cart.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userInfoDataStore: UserInfoDataStore
) : ViewModel() {

    init {
        getLocalProducts()
    }

    private val _getOrder = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getOrder = _getOrder.asStateFlow()

    private val _getProducts = MutableStateFlow<List<LocalProduct>>(emptyList())
    val getProducts = _getProducts.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun setOrder(order: Order) {
        viewModelScope.launch {
            productRepository.setOrder(order).collect {
                _getOrder.emit(it)
            }
        }
    }

    private fun getLocalProducts() {
        viewModelScope.launch {
            productRepository.getLocalProducts().collect {
                _getProducts.emit(it)
            }
        }
    }

    fun deleteAllProductsBasket() {
        viewModelScope.launch {
            productRepository.deleteAllProductsBasket()
        }
    }

}