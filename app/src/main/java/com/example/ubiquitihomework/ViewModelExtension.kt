package com.example.ubiquitihomework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubiquitihomework.misc.provider.TestCoroutineScope

fun ViewModel.getViewModelScope() =
    TestCoroutineScope ?: this.viewModelScope