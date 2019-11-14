package com.maangata.fillit_tionary.Mvvm

import android.app.Application
import androidx.lifecycle.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.Mot
import java.util.*
import kotlin.collections.ArrayList

class QuizViewModel(mLangue: String, val app: Application): AndroidViewModel(app) {

    private var dataManager: DataManager = DataManager(app)
    var mInitialLiveData: LiveData<List<Mot>>

    init {
        mInitialLiveData = dataManager.getTheMotsList(mLangue)
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    class Factory(val mLangue: String, val app: Application) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return QuizViewModel(mLangue, app) as T
        }
    }
}