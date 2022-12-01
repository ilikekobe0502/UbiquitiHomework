package com.example.ubiquitihomework.model.repository

import com.example.ubiquitihomework.model.api.ApiService
import com.example.ubiquitihomework.model.api.response.AirStatusResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException


class AirStatusRepositoryImpl(private val apiService: ApiService) :AirStatusRepository{

    override suspend fun getAirStatus(): Flow<AirStatusResponse?> {
        val queryMap: HashMap<String, String> = HashMap()
        queryMap["limit"] = "1000"
        queryMap["sort"] = "ImportDate desc"
        queryMap["format"] = "json"
        return flowOf(apiService.getAirStatus(queryMap))
            .map { result ->
                if (!result.isSuccessful) throw HttpException(result)
                return@map result.body()
            }
    }
}