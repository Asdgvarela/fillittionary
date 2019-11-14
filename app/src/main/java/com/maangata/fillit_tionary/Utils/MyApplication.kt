package com.maangata.fillit_tionary.Utils

import android.app.Application
import android.content.Context

class MyApplication: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        // It makes the application context available everywhere.
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}