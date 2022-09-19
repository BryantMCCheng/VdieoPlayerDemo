package com.bryant.videoplayerdemo

import android.app.Application
import com.bryant.videoplayerdemo.log.MultiTagTree
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(MultiTagTree())
        }
    }

    companion object {
        internal lateinit var instance: Application
    }
}