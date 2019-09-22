package com.maangata.fillit_tionary.Mvp

import android.content.Context
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot
import java.io.File

class CloserLookModelImpl(val context: Context): MainContract.CloserLook.ModelCallback {
    override fun updateSound(listener: MainContract.CloserLook.ModelCallback.OnFinishedLoadingMot, mSound: Mot, id: Long) {

        if (mSound.sonido.equals("")) {
                val sd = File(mSound.sonido)
                sd.delete()
        }

        DataManager.updateParoles(id, mSound, context)

        listener.onFinished(mSound)
    }

    override fun getTheMot(listener: MainContract.CloserLook.ModelCallback.OnFinishedLoadingMot, id: Long) {
        val mot: Mot = DataManager.palabra(id, context)

        listener.onFinished(mot)
    }
}