package com.exercise.itunessearch

import android.app.Application
import com.exercise.itunessearch.common.AppComponent
import com.exercise.itunessearch.common.AppModule
import com.exercise.itunessearch.common.DaggerAppComponent

class ITunesSearchApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(getString(R.string.base_url)))
                .build()
                .let {
                    appComponent = it
                }
    }
}