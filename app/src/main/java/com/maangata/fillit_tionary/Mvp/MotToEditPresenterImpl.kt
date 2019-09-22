package com.maangata.fillit_tionary.Mvp

import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.MotsList

class MotToEditPresenterImpl(var mView: MainContract.MotToEdit.ViewCallBack,
                             var mModel: MainContract.MotToEdit.ModelCallback)
    : MainContract.MotToEdit.PresenterCallBack,
    MainContract.MotToEdit.ModelCallback.OnFinishedLoadingMotToEdit,
    MainContract.MotToEdit.ModelCallback.OnFinishedDeletingMot,
    MainContract.MotToEdit.ModelCallback.OnFinishedSavingMot {
    override fun onFinishedSavingMot() {
        mView.onFinishedSaving()
    }

    override fun onFinishedDeletingMot() {
        mView.onFinishedDeleting()
    }

    override fun OnRequestSaveMot(id: Long, newMot: Boolean) {
        mModel.saveTheMot(this, id, newMot)
    }

    override fun OnRequestDelete(id: Long) {
        mModel.deleteMot(this, id)
    }

    override fun onFinishedMot(mMotsList: MotsList) {
        mView.setTheMot(mMotsList)
    }


    override fun OnRequestMotToEdit(id: Long, newMot: Boolean) {
        mModel.getTheMot(this, id, newMot)
    }
}