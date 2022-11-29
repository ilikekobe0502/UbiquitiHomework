package com.example.ubiquitihomework.ui.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubiquitihomework.getViewModelScope
import com.example.ubiquitihomework.misc.provider.DispatcherProvider
import com.example.ubiquitihomework.model.api.ApiResult
import com.example.ubiquitihomework.model.api.response.AirStatusRecord
import com.example.ubiquitihomework.model.repository.ApiRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PreviewViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private val _airStatusResult = MutableLiveData<ApiResult<List<AirStatusRecord>>>()
    val airStatusResult: LiveData<ApiResult<List<AirStatusRecord>>> = _airStatusResult

    private val _searchStatusResult = MutableLiveData<List<AirStatusRecord>>()
    val searchStatusResult: LiveData<List<AirStatusRecord>> = _searchStatusResult

    fun getAirStatus() {
        _airStatusResult.postValue(ApiResult.loading())
        getViewModelScope().launch(DispatcherProvider.IO) {
            apiRepository.getAirStatus()
                .catch {
                    _airStatusResult.postValue(ApiResult.error(it))
                }.collect {
                    _airStatusResult.postValue(ApiResult.success(it?.records))
                }
        }
    }

    fun getSearchAirStatus(search: String) {
        if (search == "") {
            _searchStatusResult.postValue(arrayListOf())
        } else {
            val list = (airStatusResult.value as ApiResult.Success).result?.filter {
                it.siteName.contains(search) || it.county.contains(search)
            }
            _searchStatusResult.postValue(list ?: arrayListOf())
        }
    }
}