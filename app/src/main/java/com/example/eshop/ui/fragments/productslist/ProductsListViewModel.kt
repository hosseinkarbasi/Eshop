package com.example.eshop.ui.fragments.productslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.utils.Result
import com.example.eshop.data.remote.model.Product
import com.example.eshop.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _getProductsByCategory: MutableStateFlow<Result<List<Product>>> =
        MutableStateFlow(Result.Loading())
    val getProductsByCategory = _getProductsByCategory.asStateFlow()

    fun getProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(categoryId).collect {
                _getProductsByCategory.emit(it)
            }
        }
    }

}