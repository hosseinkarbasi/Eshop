package com.example.eshop.ui.fragments.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.model.Order
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _getProducts = MutableStateFlow<List<LocalProduct>>(emptyList())
    val getProducts = _getProducts.asStateFlow()

    private val _getOrder = MutableStateFlow<Result<Order>>(Result.Success(null))
    val getOrder = _getOrder.asStateFlow()

    init {
        getLocalProducts()
    }

    fun setOrder(order: Order) {
        viewModelScope.launch {
            productRepository.setOrder(order).collect {
                _getOrder.emit(it)
            }
        }
    }

    fun deleteAllProductsBasket() {
        viewModelScope.launch {
            productRepository.deleteAllProductsBasket()
        }
    }

    private fun getLocalProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                _getProducts.emit(it)
            }
        }
    }

}