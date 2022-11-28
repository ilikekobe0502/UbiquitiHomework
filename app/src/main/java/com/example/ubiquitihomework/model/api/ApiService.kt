package com.example.ubiquitihomework.model.api

import com.example.ubiquitihomework.model.api.response.AirStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("aqx_p_432")
    suspend fun getAirStatus(@QueryMap map: HashMap<String, String>): Response<AirStatusResponse>
}