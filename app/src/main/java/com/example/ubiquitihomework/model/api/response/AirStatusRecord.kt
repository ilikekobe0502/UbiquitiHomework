package com.example.ubiquitihomework.model.api.response

import com.google.gson.annotations.SerializedName

data class AirStatusRecord(
    @SerializedName("siteid")
    val siteId: String,
    @SerializedName("sitename")
    val siteName: String,
    @SerializedName("county")
    val county: String,
    @SerializedName("pm2.5")
    val pm2_5: String,
    @SerializedName("status")
    val status: String
)
