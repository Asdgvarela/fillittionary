package com.maangata.fillit_tionary.Async

import android.os.AsyncTask
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Room.FillitDao
import com.maangata.fillit_tionary.Room.FillitDatabase

class PopulateDb(db: FillitDatabase) : AsyncTask<Void, Void, Void>() {

    private val mTmDao: FillitDao = db.fillitDao()

    override fun doInBackground(vararg p0: Void?): Void? {

        loadJSON()

        return null
    }

    private fun loadJSON() {
        val mMot = Mot().apply {
            motEn1 = "Hola"
            motEn2 = "Hello"
            nota = "-"
            tipo = "-"
            sonido = "-"
            idioma = "English"
            foto = "-"
            flag = "-"
        }

        mTmDao.insert(mMot)
    }
}