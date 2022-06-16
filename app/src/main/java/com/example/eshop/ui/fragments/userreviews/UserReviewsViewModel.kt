package com.example.eshop.ui.fragments.userreviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.DeleteReview
import com.example.eshop.data.remote.model.Review
import com.example.eshop.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewsViewModel @Inject constructor(
    private val reviewsRepository: ReviewRepository,
    userInfoDataStore: UserInfoDataStore
) :
    ViewModel() {

    private val _getUserReviews: MutableStateFlow<ResultWrapper<List<Review>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getUserReviews = _getUserReviews.asStateFlow()

    private val _getDeleteReview: MutableStateFlow<ResultWrapper<DeleteReview>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getDeleteReview = _getDeleteReview.asStateFlow()

    private val _getEditReview: MutableStateFlow<ResultWrapper<Review>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getEditReview = _getEditReview.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun deleteReview(reviewId: Int) {
        viewModelScope.launch {
            reviewsRepository.deleteReview(reviewId).collect {
                _getDeleteReview.emit(it)
            }
        }
    }

    fun editReview(reviewId: Int, review: Review) {
        viewModelScope.launch {
            reviewsRepository.editReview(reviewId, review).collect {
                _getEditReview.emit(it)
            }
        }
    }

    fun getUserReviews(userEmail: String, perPage: Int, page: Int) {
        viewModelScope.launch {
            reviewsRepository.getUserReviews(userEmail, perPage, page).collect {
                _getUserReviews.emit(it)
            }
        }
    }
}