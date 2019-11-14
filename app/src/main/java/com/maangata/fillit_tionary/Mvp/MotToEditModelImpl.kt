package com.maangata.fillit_tionary.Mvp

import android.app.Activity
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import com.maangata.fillit_tionary.Activities.EditActivity
import com.maangata.fillit_tionary.Activities.EditActivityForLangue
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.DELETE
import com.maangata.fillit_tionary.Utils.Constants.DONT_DELETE
import kotlinx.android.synthetic.main.editing.*

class MotToEditModelImpl(var context: Context): MainContract.MotToEdit.ModelCallback {
    override fun deleteMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedDeletingMot, id: Long) {
//        DataManager.delete(id, context)
        listener.onFinishedDeletingMot()
    }

    override fun saveTheMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedSavingMot, id: Long, newMot: Boolean) {
        buildAndSaveTheMot(id, newMot)
        listener.onFinishedSavingMot()
    }

    override fun getTheMot(listener: MainContract.MotToEdit.ModelCallback.OnFinishedLoadingMotToEdit, id: Long, newMot: Boolean) {
        val mMotsList = MotsList()

//        mMotsList.singleMot = DataManager.palabra(id, context)
        mMotsList.langues = getTheLangues(newMot)

        listener.onFinishedMot(mMotsList)
    }

    fun getTheLangues(newMot: Boolean): ArrayList<String> {
//        val cursor = DataManager.wantLangue(context)
        val containsLangues = ArrayList<String>()

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

    fun buildAndSaveTheMot(id: Long, newMot: Boolean) {
        val mActivity = context as EditActivity

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

        val arl = getTheLangues(newMot)
        if (arl[mActivity.spinnerLangue.selectedItemPosition] == "") {
            mot.idioma = context.getString(R.string.sinIdioma).toUpperCase()
        } else {
            mot.idioma = arl[mActivity.spinnerLangue.selectedItemPosition].toUpperCase()
        }

//        DataManager.updateParoles(mot, context)
    }
}