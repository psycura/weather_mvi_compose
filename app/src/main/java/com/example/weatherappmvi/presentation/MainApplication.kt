package com.example.weatherappmvi.presentation

import android.app.Application
import com.example.weatherappmvi.di.WeatherAppModule
import com.example.weatherappmvi.di.dataModule
import com.example.weatherappmvi.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.*

class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                dataModule,
                WeatherAppModule().module,
                presentationModule,
            )
        }
    }
}