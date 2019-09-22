package com.maangata.fillit_tionary.Mvp

import com.maangata.fillit_tionary.Interfaces.MainContract

class LanguesPresenterCallbackImpl(val mView: MainContract.Langues.ViewCallback,
                                   val mModel: MainContract.Langues.ModelCallback)
    : MainContract.Langues.PresenterCallback, MainContract.Langues.ModelCallback.OnFinishedLoadingLanguesListener {

    override fun onFinished(mList: ArrayList<String>) {
        mView.setLanguesArray(mList)
    }

    override fun onRequestingLangues() {
        mModel.getLangues(this)
    }
}