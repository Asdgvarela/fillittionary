package com.maangata.fillit_tionary.Mvp

import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot

class CloserLookPresenterImpl(var mView: MainContract.CloserLook.ViewCallback,
                              var mModel: MainContract.CloserLook.ModelCallback): MainContract.CloserLook.PresenterCallback, MainContract.CloserLook.ModelCallback.OnFinishedLoadingMot  {

    override fun OnUpdateSound(mMot: Mot, id: Long) {
        mModel.updateSound(this, mMot, id)
    }

    override fun onFinished(mMot: Mot) {
        mView.setTheMot(mMot)
    }

    override fun OnRequestMot(mot: Mot?, id: Long) {
        mModel.getTheMot(this, id)
    }
}