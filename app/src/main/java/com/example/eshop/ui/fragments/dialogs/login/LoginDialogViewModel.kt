package com.example.eshop.ui.fragments.dialogs.login

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
class LoginDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userInfoDataStore: UserInfoDataStore
) : ViewModel() {

    private val _getCustomer: MutableStateFlow<ResultWrapper<List<User>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getCustomer = _getCustomer.asStateFlow()

    fun getCustomer(email: String) {
        viewModelScope.launch {
            userRepository.getCustomer(email).collect {
                _getCustomer.emit(it)
            }
        }
    }

    fun saveUserInfo(email: String, userId: Int) {
        viewModelScope.launch {
            userInfoDataStore.saveUserInfo(email, userId)
        }
    }

}