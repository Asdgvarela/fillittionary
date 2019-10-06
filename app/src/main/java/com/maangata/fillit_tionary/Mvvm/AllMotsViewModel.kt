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
import com.maangata.fillit_tionary.Model.Mot


class AllMotsViewModel(var app: Application, var mLangue: String): AndroidViewModel(app) {

    var allMotsViewModel: MutableLiveData<ArrayList<Mot>>

    init {
        allMotsViewModel = DataManager.getTheMotsList(mLangue, app.applicationContext)
    }

    fun getAllMotsViewModel(): LiveData<ArrayList<Mot>> {
        return allMotsViewModel
    }

    fun refreshViewModel() {
        allMotsViewModel.value = DataManager.getTheMotsList(mLangue, app.applicationContext).value
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