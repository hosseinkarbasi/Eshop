package com.example.eshop.ui.fragments.cart.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    userInfoDataStore: UserInfoDataStore
) :
    ViewModel() {

    private val _getProducts = MutableStateFlow<List<LocalProduct>>(emptyList())
    val getProducts = _getProducts.asStateFlow()

    private val _getTotalPrice = MutableStateFlow(0)
    val getTotalPrice = _getTotalPrice.asStateFlow()

    val pref = userInfoDataStore.preferences

    init {
        getLocalProducts()
    }

    private fun getLocalProducts() {
        viewModelScope.launch {
            cartRepository.getLocalProducts().collect {listProducts ->
                _getProducts.emit(listProducts)

                val total = listProducts.sumOf { it.price.toInt() * it.quantity }
                _getTotalPrice.emit(total)
            }
        }
    }


    private fun deleteProduct(id: Int) {
        viewModelScope.launch {
            cartRepository.deleteProduct(id)
        }
    }

    private fun updateProduct(product: LocalProduct) {
        viewModelScope.launch {
            cartRepository.update(product)
        }
    }

    fun increase(product: LocalProduct) {
        product.quantity++
        updateProduct(product)
    }

    fun decrease(product: LocalProduct) {
        product.quantity--
        if (product.quantity <= 0) deleteProduct(product.id)
        else updateProduct(product)
    }


}