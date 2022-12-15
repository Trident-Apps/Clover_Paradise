package com.arcsys.tictacto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CloverApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}