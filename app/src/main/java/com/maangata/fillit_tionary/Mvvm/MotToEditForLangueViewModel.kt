package com.maangata.fillit_tionary.Mvvm

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditForLangueViewModel(var app: Activity, var id: Long, var newMot: Boolean): ViewModel() {

    var mMotToEditViewModel: MutableLiveData<MotsList>

    init {
        mMotToEditViewModel = MutableLiveData<MotsList>()
    }

    fun getMotToEditForLangueViewModel(): MutableLiveData<MotsList> {
        return mMotToEditViewModel
    }

    fun saveTheMotToEdit() {
        DataManager.saveTheMotForLangue(id, app)
    }

    fun deleteMot() {
        DataManager.deleteMotForLangue(id, app)
    }

    fun refreshData() {
        mMotToEditViewModel.value = DataManager.getTheMotToEdit(app, id, newMot).value
    }
    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val activity: Activity, private val id: Long, private val newMot: Boolean) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MotToEditForLangueViewModel(activity, id, newMot) as T
        }
    }
}