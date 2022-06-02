package com.example.eshop.ui.fragments.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _getProduct: MutableStateFlow<Result<Product>> =
        MutableStateFlow(Result.Loading())
    val getProduct = _getProduct.asStateFlow()

    fun getProduct(productId: Int) {
        viewModelScope.launch {
            productRepository.getProduct(productId).collect {
                _getProduct.emit(it)
            }
        }
    }

    fun insertProduct(product: LocalProduct) {
        viewModelScope.launch {
            productRepository.insertProduct(product)
        }
    }
}