package com.example.eshop.ui.fragments.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.repository.ProductRepository
import com.example.eshop.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: ProductRepository
) :
    ViewModel() {

    private val _getClothingCategory: MutableStateFlow<Result<List<Category>>> =
        MutableStateFlow(Result.Loading())
    val getClothingCategory = _getClothingCategory.asStateFlow()

    private val _getDigitalCategory: MutableStateFlow<Result<List<Category>>> =
        MutableStateFlow(Result.Loading())
    val getDigitalCategory = _getDigitalCategory.asStateFlow()

    private val _getSuperMarketCategory: MutableStateFlow<Result<List<Category>>> =
        MutableStateFlow(Result.Loading())
    val getSuperMarketCategory = _getSuperMarketCategory.asStateFlow()

    private val _getBooksAndArtCategory: MutableStateFlow<Result<List<Category>>> =
        MutableStateFlow(Result.Loading())
    val getBooksAndArtCategory = _getBooksAndArtCategory.asStateFlow()


    init {
        clothingCategory()
    }

    private fun clothingCategory() {
        repository.getCategories { clothing, digital, superMarket, booksAndArt ->
            viewModelScope.launch {
                clothing.collect {
                    _getClothingCategory.emit(it)
                }
                digital.collect {
                    _getDigitalCategory.emit(it)
                }
                superMarket.collect {
                    _getSuperMarketCategory.emit(it)
                }
                booksAndArt.collect {
                    _getBooksAndArtCategory.emit(it)
                }
            }
        }
    }

}