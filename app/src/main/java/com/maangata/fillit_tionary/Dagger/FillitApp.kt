package com.maangata.fillit_tionary.Dagger

import android.app.Application

class FillitApp: Application() {

    private lateinit var fillitComponent: FillitComponent

    override fun onCreate() {
        super.onCreate()
        // initialize Dagger
        fillitComponent = DaggerFillitComponent.builder().application(this).build()
    }

    fun getFillitApp(): FillitComponent {
        return fillitComponent
    }
}