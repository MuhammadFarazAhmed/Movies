package com.example.movies

import android.app.Application
import com.example.movies.di.NetworkModule
import com.example.movies.di.featureModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(NetworkModule + featureModules())
        }
    }
}
