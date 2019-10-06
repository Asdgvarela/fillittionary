package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.Mot

class CloserLookViewModel(var app: Application, var id: Long): AndroidViewModel(app) {

    var mCloserLookViewModel: MutableLiveData<Mot> = MutableLiveData()

    init {
        mCloserLookViewModel = DataManager.getPalabra(id, app.applicationContext)
    }

    fun getCloserViewViewModel(): LiveData<Mot> {
        return mCloserLookViewModel
    }

    fun refreshViewModel() {
        mCloserLookViewModel.value = DataManager.getPalabra(id, app.applicationContext).value
    }

    fun deleteMot() {
        DataManager.delete(id, app.applicationContext)
        refreshViewModel()
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val application: Application, private val id: Long) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return CloserLookViewModel(application, id) as T
        }
    }

}