package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maangata.fillit_tionary.Data.DataManager

class LanguesViewModel(var app: Application): AndroidViewModel(app) {

    var mLanguesViewModel: MutableLiveData<ArrayList<String>> = MutableLiveData()

    init {
        mLanguesViewModel = DataManager.getTheLangues(app.applicationContext)
    }

    fun getTheViewModel(): LiveData<ArrayList<String>> {
        return mLanguesViewModel
    }
}