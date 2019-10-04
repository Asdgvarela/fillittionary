package com.maangata.fillit_tionary.Mvvm

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditViewModel(var activity: Activity, var id: Long, var newMot: Boolean): ViewModel() {

    var mMotToEditViewModel: MutableLiveData<MotsList>

    init {
        mMotToEditViewModel = DataManager.getTheMotToEdit(activity, id, newMot)
    }

    fun getMotToEditViewModel(): LiveData<MotsList> {
        return mMotToEditViewModel
    }

    fun saveTheMotToEdit() {
        DataManager.saveTheMotToEdit(id, newMot, activity)
    }

    fun deleteMot() {
        DataManager.delete(id, activity)
    }

    fun refreshData() {
        mMotToEditViewModel.value = DataManager.getTheMotToEdit(activity, id, newMot).value
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val activity: Activity, private val id: Long, private val newMot: Boolean) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MotToEditViewModel(activity, id, newMot) as T
        }
    }
}