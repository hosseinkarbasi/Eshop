package com.example.eshop.ui.fragments.basket

import androidx.lifecycle.ViewModel
import com.example.eshop.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

}