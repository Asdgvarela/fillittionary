package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import com.maangata.fillit_tionary.Data.DataManager
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Model.Mot

class AllMotsViewModel(var app: Application, private var mLangue: String): AndroidViewModel(app) {

    private val dataManager = DataManager(app)
    private val mInitialLiveData: LiveData<List<Mot>>
    private var allMotsViewModel: MutableLiveData<List<Mot>> = MutableLiveData()
    var mMediator = MediatorLiveData<List<Mot>>()

    init {
        mInitialLiveData = dataManager.getTheMotsList(mLangue)
        mMediator.addSource(mInitialLiveData) {
            allMotsViewModel.value = it
        }
    }

    fun getAllMotsViewModel(): LiveData<List<Mot>> {
        return allMotsViewModel
    }

    fun refreshViewModel() {
        allMotsViewModel.value = dataManager.getTheMotsList(mLangue).value
    }

    class Factory(private val application: Application, private val mLangue: String) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return AllMotsViewModel(application, mLangue) as T
        }
    }
}