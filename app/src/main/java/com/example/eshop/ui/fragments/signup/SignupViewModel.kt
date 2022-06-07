package com.example.eshop.ui.fragments.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.userinfo.UserInfoDataStore
import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userInfoDataStore: UserInfoDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _getCustomerInfo: MutableStateFlow<ResultWrapper<User>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getCustomerInfo = _getCustomerInfo.asStateFlow()

    fun saveUserInfo(email: String, userId: Int) {
        viewModelScope.launch {
            userInfoDataStore.saveUserInfo(email, userId)
        }
    }

    fun createCustomer(user: User) {
        viewModelScope.launch {
            userRepository.createCustomer(user).collect {
                _getCustomerInfo.emit(it)
            }
        }
    }

}