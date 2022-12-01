package com.example.ubiquitihomework.model.repository

import com.example.ubiquitihomework.model.api.response.AirStatusResponse
import kotlinx.coroutines.flow.Flow

interface AirStatusRepository {
    suspend fun getAirStatus(): Flow<AirStatusResponse?>
}