package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.Mot
import java.io.File

class CloserLookViewModel(var app: Application, var id: Long): AndroidViewModel(app) {

    private val dataManager = DataManager(app)
    var mInitialLiveData: LiveData<Mot>
    var mCloserLookViewModel = MutableLiveData<Mot>()
    var mMediatorLiveData = MediatorLiveData<Mot>()

    init {
        mInitialLiveData = dataManager.getPalabra(id)
        mMediatorLiveData.addSource(mInitialLiveData) {
            mCloserLookViewModel.value = it
        }
    }

    fun refreshViewModel() {
        mCloserLookViewModel.value = dataManager.getPalabra(id).value
    }

    fun deleteMot(mot: Mot) {
        dataManager.delete(mot)
        refreshViewModel()
    }

    fun setTheSound(sound: String) {
        mCloserLookViewModel.value!!.sonido = sound
    }

    fun updateMot() {
        dataManager.updateParoles(mCloserLookViewModel.value!!)
    }

    fun removeSound() {
        val forSound = File(mCloserLookViewModel.value!!.sonido)
        forSound.delete()

        mCloserLookViewModel.value!!.sonido = ""
        dataManager.updateParoles(mCloserLookViewModel.value!!)
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