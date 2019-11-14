package com.maangata.fillit_tionary.Mvp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.maangata.fillit_tionary.Activities.EditActivity
import com.maangata.fillit_tionary.Activities.EditActivityForLangue
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import kotlinx.android.synthetic.main.editing.*

class MotToEditForLangueModelImpl(var context: Context): MainContract.MotToEdit.ModelCallback {
    override fun saveTheMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedSavingMot, id: Long, newMot: Boolean) {
        buildAndSaveTheMot(id)
        listener.onFinishedSavingMot()
    }

    override fun deleteMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedDeletingMot, id: Long) {
//        DataManager.delete(id, context)
        listener.onFinishedDeletingMot()

    }

    override fun getTheMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedLoadingMotToEdit, id: Long, newMot: Boolean) {
        val mMotsList = MotsList()

//        mMotsList.singleMot = DataManager.palabra(id, context)
//        mMotsList.langues = getTheLangues(newMot)
//
//        listener.onFinishedMot(mMotsList)
    }

    fun getTheLangues(newMot: Boolean): ArrayList<String> {
//        val cursor = DataManager.wantLangue(context)
        val containsLangues = ArrayList<String>()
//
//        while (cursor.moveToNext()) {
//            if (!cursor.isNull(1)) {
//
//                if (cursor.getString(1) == "") {
//                    Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
//                    if (!newMot)
//                        DataManager.delete(cursor.getLong(0), context)
//
//                } else if (cursor.getString(1) == "-") {
//                    if (!containsLangues.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
//                        containsLangues.add(cursor.getString(R.string.sinIdioma).toUpperCase())
//                    }
//
//                } else {
//                    if (!containsLangues.contains<Any>(cursor.getString(1).toUpperCase())) {
//                        containsLangues.add(cursor.getString(1).toUpperCase())
//                    }
//                }
//            }
//        }

        return containsLangues
    }

    fun buildAndSaveTheMot(id: Long) {
        val mActivity = context as EditActivityForLangue

        val mot = Mot()
        if (mActivity.motEn1E.text.toString() != "") {
            val temp =
                mActivity.motEn1E.text.toString().substring(0, 1).toUpperCase() + mActivity.motEn1E.text.toString().substring(1)
            mot.motEn1 = temp
        } else {
            mot.motEn1 = "-"
        }

        if (mActivity.motEn2E.text.toString() != "") {
            val temp =
                mActivity.motEn2E.text.toString().substring(0, 1).toUpperCase() + mActivity.motEn2E.text.toString().substring(1)
            mot.motEn2 = temp
        } else {
            mot.motEn2 = "-"
        }

        if (mActivity.tipoE.text.toString() != "") {
            val temp = mActivity.tipoE.text.toString().substring(0, 1).toUpperCase() + mActivity.tipoE.text.toString().substring(1)
            mot.tipo = temp
        } else {
            mot.tipo = "-"
        }

        if (mActivity.notaE.text.toString() != "") {
            val temp = mActivity.notaE.text.toString().substring(0, 1).toUpperCase() + mActivity.notaE.text.toString().substring(1)
            mot.nota = temp
        } else {
            mot.nota = "-"
        }

        if (mActivity.idioma.text.toString() != "") {
            mot.idioma = mActivity.idioma.text.toString()
        } else {
            mot.idioma = mActivity.getString(R.string.sinIdioma)
        }

//        DataManager.updateParoles(mot, context)
    }
}