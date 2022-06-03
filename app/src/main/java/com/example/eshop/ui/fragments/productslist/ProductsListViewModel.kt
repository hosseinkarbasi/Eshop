package com.example.eshop.ui.fragments.productslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _getProductsByCategory: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading())
    val getProductsByCategory = _getProductsByCategory.asStateFlow()

    private val _getNewestProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading())
    val getNewestProducts = _getNewestProducts.asStateFlow()

    private val _getMostViewedProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading())
    val getMostViewedProducts = _getMostViewedProducts.asStateFlow()

    private val _getMostSalesProducts: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading())
    val getMostSalesProducts = _getMostSalesProducts.asStateFlow()


    var categoryId = state.get<Int>("categoryId") ?: ""
        set(value) {
            field = value
            state.set("categoryId", value)
        }

    var productsType = state.get<String>("productsType") ?: ""
        set(value) {
            field = value
            state.set("productsType", value)
        }


    fun getNewest() {
        viewModelScope.launch {
            productRepository.getNewestProducts(100).collect {
                _getNewestProducts.emit(it)
            }
        }
    }

    fun getMostViewed() {
        viewModelScope.launch {
            productRepository.getMostViewedProducts(100).collect {
                _getMostViewedProducts.emit(it)
            }
        }
    }

    fun getMostSales() {
        viewModelScope.launch {
            productRepository.getBestSalesProducts(100).collect {
                _getMostSalesProducts.emit(it)
            }
        }
    }

    fun getProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(categoryId).collect {
                _getProductsByCategory.emit(it)
            }
        }
    }

}