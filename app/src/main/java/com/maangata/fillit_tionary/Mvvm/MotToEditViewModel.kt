package com.maangata.fillit_tionary.Mvvm

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditViewModel(var activity: Activity, var id: Long, var newMot: Boolean, app: Application): ViewModel() {

    private val dataManager = DataManager(app)
    private var mInitialSingleMot: LiveData<Mot>
    private var mInitialLangues: LiveData<List<Mot>>
    var mMotToEditViewModel = MutableLiveData<MotsList>()
    var mMediatorLiveData = MediatorLiveData<Any>()

    // Aquí debería poner un LiveData para el single mot, y otro para el langues y combinarlos con un mediator.

    init {
        mInitialSingleMot = dataManager.getSingleMot(id)
        mInitialLangues = dataManager.getTheLanguesInitial()
        mMediatorLiveData.addSource(mInitialSingleMot) {
            if (it != null) {
                var mMotsList = MotsList()
                if (mMotToEditViewModel.value != null) {
                    mMotsList = mMotToEditViewModel.value!!

                }
                mMotsList.singleMot = it
                mMotToEditViewModel.value = mMotsList
            }
        }
        mMediatorLiveData.addSource(mInitialLangues) {
            if (it != null) {
                var mMotsList = MotsList()
                if (mMotToEditViewModel.value != null) {
                    mMotsList = mMotToEditViewModel.value!!
                }
                mMotsList.langues = dataManager.getOnlyTheLanguages(it, activity)

                mMotToEditViewModel.value = mMotsList
            }
        }
    }

    fun saveTheMotToEdit(sound: String) {
        dataManager.saveTheMotToEdit(newMot, activity, sound)
    }

    fun deleteMot(mot: Mot) {
        dataManager.delete(mot)
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(private val activity: Activity, private val id: Long, private val newMot: Boolean, val app: Application) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MotToEditViewModel(activity, id, newMot, app) as T
        }
    }
}