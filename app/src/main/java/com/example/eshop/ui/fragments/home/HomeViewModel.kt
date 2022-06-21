package com.example.eshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _getNewestProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getNewestProducts = _getNewestProducts.asStateFlow()

    private val _getMostViewedProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getMostViewedProducts = _getMostViewedProducts.asStateFlow()

    private val _getMostSalesProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getMostSalesProducts = _getMostSalesProducts.asStateFlow()

    private val _getSpecialSale: MutableStateFlow<ResultWrapper<Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getSpecialSale = _getSpecialSale.asStateFlow()

    init {
        getNewestProducts()
        getMostViewed()
        getMostSales()
    }

    fun retry() {
        if (_getMostViewedProducts.value !is ResultWrapper.Success) getMostViewed()
        if (_getNewestProducts.value !is ResultWrapper.Success) getNewestProducts()
        if (_getMostSalesProducts.value !is ResultWrapper.Success) getMostSales()
    }

    fun getSpecialProduct(productId: Int) {
        viewModelScope.launch {
            productRepository.getProduct(productId).collect {
                _getSpecialSale.emit(it)
            }
        }
    }

    private fun getNewestProducts() {
        viewModelScope.launch {
            productRepository.getNewestProducts(10, 1).collect {
                _getNewestProducts.emit(it)
            }
        }
    }

    private fun getMostViewed() {
        viewModelScope.launch {
            productRepository.getMostViewedProducts(10, 1).collect {
                _getMostViewedProducts.emit(it)
            }
        }
    }

    private fun getMostSales() {
        viewModelScope.launch {
            productRepository.getBestSalesProducts(10, 1).collect {
                _getMostSalesProducts.emit(it)
            }
        }
    }

    fun isSuccess(): Boolean {
        return _getMostSalesProducts.value is ResultWrapper.Success &&
                _getNewestProducts.value is ResultWrapper.Success &&
                _getMostViewedProducts.value is ResultWrapper.Success
    }

    fun isError(): Boolean {
        return _getMostSalesProducts.value is ResultWrapper.Error &&
                _getNewestProducts.value is ResultWrapper.Error &&
                _getMostViewedProducts.value is ResultWrapper.Error
    }

    fun isLoading(): Boolean {
        return _getMostSalesProducts.value is ResultWrapper.Loading &&
                _getNewestProducts.value is ResultWrapper.Loading &&
                _getMostViewedProducts.value is ResultWrapper.Loading
    }
}