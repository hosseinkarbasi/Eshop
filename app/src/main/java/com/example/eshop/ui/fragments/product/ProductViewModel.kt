package com.example.eshop.ui.fragments.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.remote.model.SetOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    userInfoDataStore: UserInfoDataStore
) : ViewModel() {

    private val _getProduct: MutableStateFlow<ResultWrapper<Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct = _getProduct.asStateFlow()

    private val _getOrder = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getOrder = _getOrder.asStateFlow()

    private val _getOrderList = MutableStateFlow<ResultWrapper<List<Order>>>(ResultWrapper.Loading)
    val getOrderList = _getOrderList.asStateFlow()

    val pref = userInfoDataStore.preferences


    fun updateOrder(orderId: Int, order: SetOrder) {
        viewModelScope.launch {
            productRepository.updateOrder(orderId, order).collect {
                _getOrder.emit(it)
            }
        }
    }

    fun getOrders(userId: Int, status: String) {
        viewModelScope.launch {
            productRepository.getOrders(userId, status).collect {
                _getOrderList.emit(it)
            }
        }
    }

    fun getProduct(productId: Int) {
        viewModelScope.launch {
            productRepository.getProduct(productId).collect {
                _getProduct.emit(it)
            }
        }
    }

//    fun insertProduct(product: LocalProduct) {
//        viewModelScope.launch {
//            productRepository.insertProduct(product)
//        }
//    }

    fun setOrder(order: SetOrder) {
        viewModelScope.launch {
            productRepository.setOrder(order).collect {
                _getOrder.emit(it)
            }
        }
    }
}