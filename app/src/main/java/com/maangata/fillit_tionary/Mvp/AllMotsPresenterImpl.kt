package com.maangata.fillit_tionary.Mvp

import android.database.Cursor
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.MotsList

class AllMotsPresenterImpl(var mView: MainContract.AllMots.ViewCallback,
                           var mModel: MainContract.AllMots.ModelCallback)
    : MainContract.AllMots.PresenterCallback, MainContract.AllMots.ModelCallback.OnFinishedLoadingTheMotsList {

    override fun onFinished(mCursor: Cursor) {
        mView.setTheMotList(mCursor)
    }

    override fun onRequestingAllMots(langue: String) {
        mModel.getTheMotsList(this, langue)
    }
}