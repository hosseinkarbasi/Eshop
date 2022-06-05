package com.example.eshop.ui.fragments.search

import androidx.lifecycle.SavedStateHandle
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
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    var searchText = state.get<String>("query") ?: ""
        set(value) {
            field = value
            state.set("query", value)
        }


    private val _getSearchText: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getSearchText = _getSearchText.asStateFlow()

    fun searchProducts(searchText: String, orderBy: String,order: String) {
        viewModelScope.launch {
            productRepository.searchProducts(searchText, orderBy,order).collect {
                _getSearchText.emit(it)
            }
        }
    }
}