package com.example.ubiquitihomework.di

import android.content.Context
import com.example.ubiquitihomework.model.repository.AirStatusRepository
import com.example.ubiquitihomework.model.repository.AirStatusRepositoryImpl
import com.example.ubiquitihomework.network.ApiModule
import com.example.ubiquitihomework.network.AuthInterceptor
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
                        networkModule,
                        viewModelModule
                    )
                )
            }
        }
    }
}

val networkModule = module {
    single { AuthInterceptor() }
    single { ApiModule.provideOkHttpClient(get()) }
    single { ApiModule.provideRetrofit(get()) }
    single { ApiModule.provideAirStatusApi(get()) }
    factory<AirStatusRepository> { AirStatusRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { PreviewViewModel(get()) }
}