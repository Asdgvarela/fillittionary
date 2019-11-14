package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.Mot

class LanguesViewModel(var app: Application): AndroidViewModel(app) {

    private val dataManager = DataManager(app)
    private var mInitialLiveData: LiveData<List<Mot>>
    private var mLanguesViewModel: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var mMediator: MediatorLiveData<List<Mot>>

    init {
        mInitialLiveData = dataManager.getTheLanguesInitial()
        mMediator = MediatorLiveData()
        mMediator.addSource(mInitialLiveData) {
            mLanguesViewModel.value = dataManager.getOnlyTheLanguages(it, app.applicationContext)
        }
    }

    fun getTheViewModel(): LiveData<ArrayList<String>> {
        return mLanguesViewModel
    }

    fun refreshLangues() {
        Transformations.switchMap(dataManager.getTheLanguesInitial()) { list ->
            mLanguesViewModel.value = dataManager.getOnlyTheLanguages(list, app.applicationContext)
            mLanguesViewModel
        }
    }
}