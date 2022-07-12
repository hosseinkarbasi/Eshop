package com.example.eshop.data.remote.model

import com.google.gson.annotations.SerializedName as SN

data class NeshanAddress (
    @SN("neighbourhood")
    var neighbourhood: String? = null,
    @SN("formatted_address")
    var address: String? = null,
    @SN("municipality_zone")
    var municipality_zone: String? = null,
    @SN("in_traffic_zone")
    var in_traffic_zone: Boolean? = null,
    @SN("in_odd_even_zone")
    var in_odd_even_zone: Boolean? = null,
    @SN("city")
    var city: String? = null,
    @SN("state")
    var state: String? = null,

    // when address is not found
    @SN("status")
    var status: String? = null,
    @SN("code")
    var code: Int? = null,
    @SN ("message")
    var message: String? = null,
)