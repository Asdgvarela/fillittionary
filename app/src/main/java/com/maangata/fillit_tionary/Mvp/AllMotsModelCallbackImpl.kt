package com.maangata.fillit_tionary.Mvp

import android.content.Context
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract

class AllMotsModelCallbackImpl(val context: Context): MainContract.AllMots.ModelCallback {

    override fun getTheMotsList(listener: MainContract.AllMots.ModelCallback.OnFinishedLoadingTheMotsList, mLangue: String) {
        val db = DataManager.createDB(context).readableDatabase
        val mCursor = db.rawQuery("SELECT * FROM fillittionary WHERE idioma = '$mLangue' COLLATE NOCASE", null)

        listener.onFinished(mCursor)
    }
}