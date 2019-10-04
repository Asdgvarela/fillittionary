package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maangata.fillit_tionary.Data.DataManager
import androidx.lifecycle.ViewModel
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider



class AllMotsViewModel(var app: Application, mLangue: String): AndroidViewModel(app) {

    var allMotsViewModel: MutableLiveData<Cursor> = MutableLiveData()

    init {
        allMotsViewModel = DataManager.getTheMotsList(mLangue, app.applicationContext)
    }

    fun getAllMotsViewModel(): LiveData<Cursor> {
        return  allMotsViewModel
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val application: Application, private val mLangue: String) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return AllMotsViewModel(application, mLangue) as T
        }
    }

}