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

    private val _getProduct: MutableStateFlow<Result<List<Product>>> =
        MutableStateFlow(Result.Loading())
    val getProduct = _getProduct.asStateFlow()

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                _getProduct.emit(it)
            }
        }
    }
}