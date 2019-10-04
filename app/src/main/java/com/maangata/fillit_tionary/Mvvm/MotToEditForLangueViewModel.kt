package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditForLangueViewModel(var app: Application, var id: Long, var newMot: Boolean): AndroidViewModel(app) {

    var mMotToEditViewModel: MutableLiveData<MotsList>

    init {
        mMotToEditViewModel = DataManager.getTheMotToEditForLangue(id, newMot, app.applicationContext)
    }

    fun getMotToEditForLangueViewModel(): MutableLiveData<MotsList> {
        return mMotToEditViewModel
    }

    fun saveTheMotToEdit() {
        DataManager.saveTheMotForLangue(id, app.applicationContext)
    }

    fun deleteMot() {
        DataManager.deleteMotForLangue(id, app.applicationContext)
    }

    fun refreshData() {
        mMotToEditViewModel.value = DataManager.getTheMotToEdit(app.applicationContext, id, newMot).value
    }
    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val application: Application, private val id: Long, private val newMot: Boolean) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MotToEditForLangueViewModel(application, id, newMot) as T
        }
    }
}