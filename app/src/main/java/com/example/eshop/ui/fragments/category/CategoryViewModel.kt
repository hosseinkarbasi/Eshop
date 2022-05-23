package com.example.eshop.ui.fragments.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _getClothingCategory: MutableStateFlow<Result<List<Category>>> =
        MutableStateFlow(Result.Loading())
    val getClothingCategory = _getClothingCategory.asStateFlow()


    init {
        clothingCategory()
    }

    private fun clothingCategory() {
        viewModelScope.launch {
            repository.getCategories().collect {
                _getClothingCategory.emit(it)
            }
        }
    }
}