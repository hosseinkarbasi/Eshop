package com.example.eshop.utils.conntectivitymanager

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}