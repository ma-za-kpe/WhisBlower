package com.maku.whisblower

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.maku.whisblower.utils.isNight
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

        // Get UI mode and set
        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

}