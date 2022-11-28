package com.example.ubiquitihomework.di

import android.content.Context
import com.example.ubiquitihomework.ui.preview.PreviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinModules {
    companion object {
        fun initKoin(app: Context) {
            startKoin {
                androidContext(app)
                modules(
                    listOf(
                        viewModelModule
                    )
                )
            }
        }
    }
}

val viewModelModule = module {
    viewModel { PreviewViewModel() }
}