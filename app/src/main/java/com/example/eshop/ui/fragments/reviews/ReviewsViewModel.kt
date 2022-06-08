package com.example.eshop.ui.fragments.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Review
import com.example.eshop.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _getReviewsList: MutableStateFlow<ResultWrapper<List<Review>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getReviewsList = _getReviewsList.asStateFlow()

    var productId = state.get<Int>("product_id") ?: ""
        set(value) {
            field = value
            state.set("product_id", value)
        }

    fun getReviews(productId: Int, page: Int, perPage: Int) {
        viewModelScope.launch {
            reviewRepository.getReviews(productId, perPage, page).collect {
                _getReviewsList.emit(it)
            }
        }
    }
}