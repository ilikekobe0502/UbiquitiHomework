package com.example.ubiquitihomework.ui.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubiquitihomework.model.api.ApiResult
import com.example.ubiquitihomework.model.api.response.AirStatusRecord
import com.example.ubiquitihomework.model.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PreviewViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private val _airStatusResult = MutableLiveData<ApiResult<ArrayList<AirStatusRecord>>>()
    val airStatusResult: LiveData<ApiResult<ArrayList<AirStatusRecord>>> = _airStatusResult

    fun getAirStatus() {
        _airStatusResult.postValue(ApiResult.loading())
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getAirStatus().catch {
                _airStatusResult.postValue(ApiResult.error(it))
            }.collect {
                _airStatusResult.postValue(ApiResult.success(it?.records))
            }
        }
    }
}