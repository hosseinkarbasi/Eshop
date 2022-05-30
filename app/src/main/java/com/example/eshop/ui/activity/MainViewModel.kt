package com.example.eshop.ui.activity

import androidx.lifecycle.ViewModel
import com.example.eshop.data.local.datastore.SettingDataStore
import com.example.eshop.utils.conntectivitymanager.MyState
import com.example.eshop.utils.conntectivitymanager.NetworkStatusTracker
import com.example.eshop.utils.conntectivitymanager.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingDataStore: SettingDataStore,
    networkStatusTracker: NetworkStatusTracker

) : ViewModel() {

    val preferences = settingDataStore.preferences

    val state = networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )
}

