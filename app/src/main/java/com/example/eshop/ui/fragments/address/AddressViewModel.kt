package com.example.eshop.ui.fragments.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.Category
import com.example.eshop.data.remote.model.NeshanAddress
import com.example.eshop.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _getAddress: MutableStateFlow<ResultWrapper<NeshanAddress>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getAddress = _getAddress.asStateFlow()

    fun getAddress(lat: Double?, lng: Double?) {
        viewModelScope.launch {
            userRepository.getAddress(lat, lng).collect {
                _getAddress.emit(it)
            }
        }
    }
}