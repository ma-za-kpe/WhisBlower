package com.maku.whisblower

import android.app.Application
import android.content.Context
import timber.log.Timber

class WhisBlower : Application(){

    //context
    init {
        instance = this
    }

    companion object {
        private var instance: WhisBlower? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        //timber
        Timber.plant(Timber.DebugTree())

        //fonts

    }

}