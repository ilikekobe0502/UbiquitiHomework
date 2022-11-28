package com.example.ubiquitihomework.model.api.response

import com.google.gson.annotations.SerializedName

data class AirStatusResponse(
    @SerializedName("SiteId")
    val siteId: String,
    @SerializedName("SiteName")
    val siteName: String,
    @SerializedName("County")
    val county: String,
    @SerializedName("PM2.5")
    val pm2_5: String,
    @SerializedName("Status")
    val status: String
)
