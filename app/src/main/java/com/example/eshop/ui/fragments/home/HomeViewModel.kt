package com.example.eshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.util.Result
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


    init {
        getProducts()
    }


    private fun getProducts() {
        productRepository.getProducts { new, most, best ->

            viewModelScope.launch {
                new.collect {
                    _getNewestProducts.emit(it)
                }
                most.collect {
                    _getMostViewedProducts.emit(it)
                }
                best.collect {
                    _getMostSalesProducts.emit(it)
                }
            }
        }
    }
}