package com.example.eshop.ui.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.data.local.datastore.settings.SettingDataStore
import com.example.eshop.data.local.datastore.settings.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingDataStore: SettingDataStore) :
    ViewModel() {

    val preferences = settingDataStore.preferences

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }

}