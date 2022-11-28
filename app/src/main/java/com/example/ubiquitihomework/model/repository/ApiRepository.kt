package com.example.ubiquitihomework.model.repository

import com.example.ubiquitihomework.model.api.ApiService
import com.example.ubiquitihomework.model.api.response.AirStatusResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber


class ApiRepository(private val apiService: ApiService) {

    suspend fun getAirStatus(): Flow<AirStatusResponse?> {
        val queryMap: HashMap<String, String> = HashMap()
        queryMap["limit"] = "1000"
        queryMap["sort"] = "ImportDate desc"
        queryMap["format"] = "json"
        Timber.d("${Thread.currentThread()} getAirStatus")
        return flowOf(apiService.getAirStatus(queryMap))
            .map { result ->
                Timber.d("${Thread.currentThread()} map")
                if (!result.isSuccessful) throw HttpException(result)
                return@map result.body()
            }
    }
}