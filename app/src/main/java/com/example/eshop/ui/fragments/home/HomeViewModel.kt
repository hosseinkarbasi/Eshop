package com.example.eshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _getNewestProducts: MutableStateFlow<Result<List<Product>>> =
        MutableStateFlow(Result.Loading())
    val getNewestProducts = _getNewestProducts.asStateFlow()

    private val _getMostViewedProducts: MutableStateFlow<Result<List<Product>>> =
        MutableStateFlow(Result.Loading())
    val getMostViewedProducts = _getMostViewedProducts.asStateFlow()

    private val _getMostSalesProducts: MutableStateFlow<Result<List<Product>>> =
        MutableStateFlow(Result.Loading())
    val getMostSalesProducts = _getMostSalesProducts.asStateFlow()

    private val _getSpecialSale: MutableStateFlow<Result<Product>> =
        MutableStateFlow(Result.Loading())
    val getSpecialSale = _getSpecialSale.asStateFlow()


    init {
        getNewestProducts()
        getMostViewed()
        getMostSales()
    }


    fun getProduct(productId: Int) {
        viewModelScope.launch {
            productRepository.getProduct(productId).collect {
                _getSpecialSale.emit(it)
            }
        }
    }

    private fun getNewestProducts() {
        viewModelScope.launch {
            productRepository.getNewestProducts(10).collect {
                _getNewestProducts.emit(it)
            }
        }
    }

    private fun getMostViewed() {
        viewModelScope.launch {
            productRepository.getMostViewedProducts(10).collect {
                _getMostViewedProducts.emit(it)
            }
        }
    }

    private fun getMostSales() {
        viewModelScope.launch {
            productRepository.getBestSalesProducts(10).collect {
                _getMostSalesProducts.emit(it)
            }
        }
    }

}