package com.example.ubiquitihomework

import android.app.Application
import com.example.ubiquitihomework.di.KoinModules.Companion.initKoin
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initKoin(this)
        Timber.plant(Timber.DebugTree())
    }
}