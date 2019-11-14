package com.maangata.fillit_tionary.Data

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maangata.fillit_tionary.Activities.EditActivity
import com.maangata.fillit_tionary.Activities.EditActivityForLangue
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Room.FillitDatabase
import com.maangata.fillit_tionary.Room.FillitDatabase.Companion.getDatabase
import com.maangata.fillit_tionary.Utils.Constants
import java.io.File
import kotlin.collections.ArrayList

/**
 * Created by zosdam on 3/09/15.
 */
class DataManager(app: Application) {

    var fillitDatabase: FillitDatabase = getDatabase(app.applicationContext)!!

    fun getFillitDb(): FillitDatabase {
        return fillitDatabase
    }

    fun getPalabra(id: Long): LiveData<Mot> {
        return fillitDatabase.fillitDao().getPalabra(id)
    }

    fun updateParoles(mot: Mot) {
        mot.flag = ""
        fillitDatabase.fillitDao().update(mot)
    }

    private fun insertParoles(mot: Mot) {
        mot.flag = ""
        fillitDatabase.fillitDao().insert(mot)
    }

    fun newMot(): Long {
        val mot = Mot()
        mot.flag = "newMot"
        val id = fillitDatabase.fillitDao().insert(mot).run {
            fillitDatabase.fillitDao().getIdFromNewWord()
        }

        return id
    }

    fun delete(id: Long) {
        val mot = fillitDatabase.fillitDao().getPalabra(id)
    }

    fun delete(mot: Mot) {
        fillitDatabase.fillitDao().delete(mot)
    }

    private fun wantLangueInitial(): LiveData<List<Mot>> {
        return fillitDatabase.fillitDao().getLanguesMots()
    }

    fun getOnlyTheLanguages(list: List<Mot>, context: Context): ArrayList<String> {
        val motList = ArrayList<String>()

        for (mMot in list) {
            if (mMot.idioma.equals("")) {
                Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                delete(mMot.id!!)

            } else if (mMot.idioma.equals("-")) {
                if (!motList.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
                    motList.add(context.getString(R.string.sinIdioma).toUpperCase())
                }

            } else {
                if (!motList.contains<Any>(mMot.idioma!!.toUpperCase())) {
                    motList.add(mMot.idioma!!.toUpperCase())
                }
            }
        }

        return motList
    }

    fun searchPalabra(palabra: String): Long {
        return fillitDatabase.fillitDao().searchPalabra(palabra)
    }

    fun searchTraduccion(palabra: String): Long {
        return fillitDatabase.fillitDao().searchTraduccion(palabra)
    }

    fun getTheLanguesInitial(): LiveData<List<Mot>> {
        val cursor = wantLangueInitial()

        return cursor
    }

    fun getTheMotsList(mLangue: String): LiveData<List<Mot>> {
        return fillitDatabase.fillitDao().searchByLangue(mLangue)
    }

    fun getSingleMot(id: Long): LiveData<Mot> {
        return fillitDatabase.fillitDao().getPalabra(id)
    }

    fun saveTheMotToEdit(newMot: Boolean, context: Activity, sound: String) {
        buildAndSaveTheMot(newMot, context, sound)
    }

    private fun buildAndSaveTheMot(newMot: Boolean, context: Activity, sound: String) {
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

        val arl = context.mMotToEditViewModel.mMotToEditViewModel.value!!.langues
        if (arl[mActivity.spinnerLangue.selectedItemPosition] == "") {
            mot.idioma = context.getString(R.string.sinIdioma).toUpperCase()
        } else {
            mot.idioma = arl[mActivity.spinnerLangue.selectedItemPosition].toUpperCase()
        }

        mot.sonido = sound

        if (newMot) {
            insertParoles(mot)
        } else {
            mot.id = context.id
            updateParoles(mot)
        }
    }

    private fun buildAndSaveTheMotForLangue(context: Activity, sound: String) {
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

        mot.sonido = sound

        insertParoles(mot)
    }

    fun saveTheMotForLangue(context: Activity, sound: String) {
        buildAndSaveTheMotForLangue(context, sound)
    }

    fun deleteMotForLangue(id: Long) {
        delete(id)
    }
}

