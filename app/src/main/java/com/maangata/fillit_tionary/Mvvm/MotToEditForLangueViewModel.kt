package com.maangata.fillit_tionary.Mvvm

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditForLangueViewModel(var activity: Activity, var id: Long, var newMot: Boolean, var app: Application): AndroidViewModel(app) {

    var mMotToEditViewModel: MutableLiveData<MotsList> = MutableLiveData<MotsList>()
    private val dataManager = DataManager(app)

    fun saveTheMotToEdit(sound: String) {
        dataManager.saveTheMotForLangue(activity, sound)
    }

    fun deleteMot() {
        dataManager.deleteMotForLangue(id)
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val activity: Activity, private val id: Long, private val newMot: Boolean, val app: Application) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MotToEditForLangueViewModel(activity, id, newMot, app) as T
        }
    }
}