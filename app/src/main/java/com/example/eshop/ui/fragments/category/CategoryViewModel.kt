package com.example.eshop.ui.fragments.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) :
    ViewModel() {

    private val _getClothingCategory: MutableStateFlow<ResultWrapper<List<Category>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getClothingCategory = _getClothingCategory.asStateFlow()

    private val _getDigitalCategory: MutableStateFlow<ResultWrapper<List<Category>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getDigitalCategory = _getDigitalCategory.asStateFlow()

    private val _getSuperMarketCategory: MutableStateFlow<ResultWrapper<List<Category>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getSuperMarketCategory = _getSuperMarketCategory.asStateFlow()

    private val _getBooksAndArtCategory: MutableStateFlow<ResultWrapper<List<Category>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getBooksAndArtCategory = _getBooksAndArtCategory.asStateFlow()


    init {
        getClothingList()
        getDigitalList()
        getSuperMarketList()
        getBooksAndArtList()
    }

    fun retry() {
        if (_getClothingCategory.value !is ResultWrapper.Success) getClothingList()
        if (_getDigitalCategory.value !is ResultWrapper.Success) getDigitalList()
        if (_getSuperMarketCategory.value !is ResultWrapper.Success) getSuperMarketList()
        if (_getBooksAndArtCategory.value !is ResultWrapper.Success) getBooksAndArtList()
    }

    private fun getClothingList() {
        viewModelScope.launch {
            repository.getClothingList().collect {
                _getClothingCategory.emit(it)
            }
        }
    }

    private fun getDigitalList() {
        viewModelScope.launch {
            repository.getDigitalList().collect {
                _getDigitalCategory.emit(it)
            }
        }
    }

    private fun getSuperMarketList() {
        viewModelScope.launch {
            repository.getSuperMarketList().collect {
                _getSuperMarketCategory.emit(it)
            }
        }
    }

    private fun getBooksAndArtList() {
        viewModelScope.launch {
            repository.getBooksAndArtList().collect {
                _getBooksAndArtCategory.emit(it)
            }
        }
    }

    fun isSuccess(): Boolean {
        return _getClothingCategory.value is ResultWrapper.Success &&
                _getDigitalCategory.value is ResultWrapper.Success &&
                _getSuperMarketCategory.value is ResultWrapper.Success &&
                _getBooksAndArtCategory.value is ResultWrapper.Success
    }

    fun isError(): Boolean {
        return _getClothingCategory.value is ResultWrapper.Error &&
                _getDigitalCategory.value is ResultWrapper.Error &&
                _getSuperMarketCategory.value is ResultWrapper.Error &&
                _getBooksAndArtCategory.value is ResultWrapper.Error
    }

    fun isLoading(): Boolean {
        return _getClothingCategory.value is ResultWrapper.Loading &&
                _getDigitalCategory.value is ResultWrapper.Loading &&
                _getSuperMarketCategory.value is ResultWrapper.Loading &&
                _getBooksAndArtCategory.value is ResultWrapper.Loading
    }

}