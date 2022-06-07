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

    private val _getProductsList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductsList = _getProductsList.asStateFlow()


    var productsType = state.get<String>("productsType") ?: ""
        set(value) {
            field = value
            state.set("productsType", value)
        }


    fun getNewest(perPage: Int, page: Int) {
        viewModelScope.launch {
            productRepository.getNewestProducts(perPage, page).collect {
                _getProductsList.emit(it)
            }
        }
    }

    fun getMostViewed(perPage: Int, page: Int) {
        viewModelScope.launch {
            productRepository.getMostViewedProducts(perPage, page).collect {
                _getProductsList.emit(it)
            }
        }
    }

    fun getMostSales(perPage: Int, page: Int) {
        viewModelScope.launch {
            productRepository.getBestSalesProducts(perPage, page).collect {
                _getProductsList.emit(it)
            }
        }
    }

    fun getProductsByCategory(categoryId: Int,perPage: Int, page: Int) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(categoryId,perPage, page).collect {
                _getProductsList.emit(it)
            }
        }
    }
}