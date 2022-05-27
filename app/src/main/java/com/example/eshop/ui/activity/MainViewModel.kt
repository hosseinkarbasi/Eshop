package com.example.eshop.ui.activity

import androidx.lifecycle.ViewModel
import com.example.eshop.data.local.datastore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingDataStore: SettingDataStore
) : ViewModel() {

    val preferences = settingDataStore.preferences
}