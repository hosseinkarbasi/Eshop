package com.example.eshop.ui.fragments.profile

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
class ProfileViewModel @Inject constructor(
    userInfoDataStore: UserInfoDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _getCustomer: MutableStateFlow<ResultWrapper<User>> =
        MutableStateFlow(ResultWrapper.Loading())
    val getCustomer = _getCustomer.asStateFlow()

    val pref = userInfoDataStore.preferences

    fun getCustomer(email: String) {
        viewModelScope.launch {
            userRepository.getCustomer(email).collect {
                _getCustomer.emit(it)
            }
        }
    }
}