package com.maangata.fillit_tionary.Data

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.maangata.fillit_tionary.Activities.EditActivity
import com.maangata.fillit_tionary.Activities.EditActivityForLangue
import com.maangata.fillit_tionary.Interfaces.MainContract

import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Sql.SqlitoDBHelper
import java.io.File

/**
 * Created by zosdam on 3/09/15.
 */
object DataManager {

    private lateinit var sqlitoDBHelper: SqlitoDBHelper

    // Método que crea un objeto sqlitodb que inicia una DB.
    fun createDB(context: Context): SqlitoDBHelper {
        sqlitoDBHelper = SqlitoDBHelper(context)
        return sqlitoDBHelper
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método muy importante: crea un objeto Mot, que contiene la info de la palabra, a partir de un id, lo cuál sirve
     * para trabajar con elementos del ListView.
     * @param id: es un long que indica el elemento de la DB en el que estamos.
     * @return Devuelve un objeto Mot llenito de info.
     */
    fun palabra(id: Long, context: Context): Mot {
        val mot = Mot()
        val db = createDB(context).writableDatabase
        val cursor = db.rawQuery("SELECT * FROM fillittionary WHERE _id = $id", null)
        if (cursor.moveToNext()) {
            mot.motEn1 = cursor.getString(1)
            mot.motEn2 = cursor.getString(2)
            mot.tipo = cursor.getString(3)
            mot.nota = cursor.getString(4)
            mot.idioma = cursor.getString(6)
            mot.foto = cursor.getString(7)
            mot.sonido = cursor.getString(8)
            mot.id = id
        }
        cursor.close()
        db.close()
        return mot
    }

    fun getPalabra(id: Long, context: Context): MutableLiveData<Mot> {
        val mot = Mot()
        val db = createDB(context).writableDatabase
        val cursor = db.rawQuery("SELECT * FROM fillittionary WHERE _id = $id", null)
        while (cursor.moveToNext()) {
            mot.motEn1 = cursor.getString(1)
            mot.motEn2 = cursor.getString(2)
            mot.tipo = cursor.getString(3)
            mot.nota = cursor.getString(4)
            mot.idioma = cursor.getString(6)
            mot.foto = cursor.getString(7)
            mot.sonido = cursor.getString(8)
            mot.id = id
        }
        db.close()

        val mValueToReturn = MutableLiveData<Mot>()
        mValueToReturn.value = mot
        cursor.close()
        return mValueToReturn
    }

    fun updateSound(mSound: Mot, id: Long, context: Context) {
        if (mSound.sonido.equals("")) {
            val sd = File(mSound.sonido)
            sd.delete()
        }

        updateParoles(id, mSound, context)

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Refresca un objeto Mot con un id dado.
     * @param id: identificador del elemento sobre el que trabajamos, es un long. Sirve para encontrar el elemento sobre el que
     * vamos a realizar cambios.
     * @param mot: info que se va a actualizar en la DB.
     */
    fun updateParoles(id: Long, mot: Mot, context: Context) {
        val db = createDB(context).writableDatabase
        db.execSQL(
            "UPDATE fillittionary SET palabra = '" + mot.motEn1 +
                    "', traduccion = '" + mot.motEn2 +
                    "', funcion = '" + mot.tipo +
                    "', nota = '" + mot.nota +
                    "', flag = 'null'" +
                    ", idioma = '" + mot.idioma +
                    "', foto = '" + mot.foto +
                    "', sonido = '" + mot.sonido + "' COLLATE NOCASE" +
                    " WHERE _id = " + id
        )
        db.close()
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Al crear un int id = -1 se dice que se crea un objeto nuevo, con un id nuevo, ya que se está pasando un id inexistente.
     * El id = -1 quiere decir que es un id inexistente.
     * @return Devuelve el id de la nueva palabra, en forma de int.
     */
    fun newMot(context: Context): Int {
        var id = -1
        val db = createDB(context).writableDatabase
        db.execSQL(
            "INSERT INTO fillittionary (palabra, traduccion, funcion, nota, flag, idioma, foto, sonido) VALUES (''," + "'', " + "'', " +
                    "'', " + "'newMot', '', '', '')"
        )
        val c = db.rawQuery("SELECT _id FROM fillittionary WHERE flag = 'newMot' COLLATE NOCASE", null)
        if (c.moveToNext()) {
            id = c.getInt(0)
        }

        c.close()
        db.close()
        return id
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun delete(id: Long, context: Context) {
        val db = createDB(context).writableDatabase
        db.execSQL("DELETE FROM fillittionary WHERE _id = $id")
        db.close()
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun wantLangue(context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT _id, idioma FROM fillittionary", null)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun wantItAll(context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary", null)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun wantItAllWritable(context: Context): Cursor {
        val db = createDB(context).writableDatabase
        return db.rawQuery("SELECT * FROM fillittionary", null)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun sortByPal(context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary ORDER BY palabra ASC", null)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun sortByTrad(context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary ORDER BY traduccion ASC", null)
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Rehacer, más bien acabar.
    fun sortByFunc(context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary WHERE idioma ORDER BY funcion ASC", null)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sirve para buscar una palabra en la base de datos. Una Palabra, primer campo de la db.
     * @param palabra Es la palabra que se ha introducido en un EditText, la palabara que se quiere buscar.
     * @return Devuelve el long identificador de la palabra que se ha encontrado en la base de datos.
     * Si no se ha encontrado la palabra devuelve un -1.
     */
    fun searchPalabra(palabra: String, context: Context): Long? {
        var id: Long = -1
        val db = createDB(context).writableDatabase
        val c = db.rawQuery("SELECT * FROM fillittionary WHERE palabra = '$palabra' COLLATE NOCASE", null)
        if (c.moveToNext()) {
            id = c.getLong(0)
        }
        c.close()
        db.close()
        return id

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sirve para buscar una palabra en la base de datos. Una Traduccion, primer campo de la db.
     * @param palabra Es la palabra que se ha introducido en un EditText, la palabara que se quiere buscar.
     * @return Devuelve el long identificador de la palabra que se ha encontrado en la base de datos.
     * Si no se ha encontrado la palabra devuelve un -1.
     */
    fun searchTraduccion(palabra: String, context: Context): Long {
        var id: Long = -1
        val db = createDB(context).writableDatabase
        val c = db.rawQuery("SELECT * FROM fillittionary WHERE traduccion = '$palabra' COLLATE NOCASE", null)
        if (c.moveToNext()) {
            id = c.getLong(0)
        }
        c.close()
        db.close()
        return id

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Método para generar el cursor que servirá para generar la lista de palabras por idioma en MainActivity.
     * Se le pasa la String, que a su vez viene de PreMainActivity, que es lo que se busca en la DB.
     * @param string
     * @return
     */
    fun searchByLangue(string: String, context: Context): Cursor {
        val db = createDB(context).readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary WHERE idioma = '$string' COLLATE NOCASE", null)

    }

    fun getMotsListAllMots(context: Context, mLangue: String): MotsList {
        val db = DataManager.createDB(context).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM fillittionary WHERE idioma = '$mLangue' COLLATE NOCASE", null)

        val motsList = MotsList()
        while (cursor.moveToNext()) {
            val mot = Mot()
            mot.motEn1 = cursor.getString(1)
            mot.motEn2 = cursor.getString(2)
            mot.tipo = cursor.getString(3)
            mot.nota = cursor.getString(4)
            mot.idioma = cursor.getString(6)
            mot.foto = cursor.getString(7)
            mot.sonido = cursor.getString(8)
            mot.id = cursor.getLong(0)
            motsList.motsList.add(mot)
        }

        return motsList
    }

    fun getTheLangues(context: Context): MutableLiveData<ArrayList<String>> {
        val motList = ArrayList<String>()

        val cursor = wantLangue(context)

        while (cursor.moveToNext()) {
            if (!cursor.isNull(1)) {

                if (cursor.getString(1) == "") {
                    Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                    delete(cursor.getLong(0), context)

                } else if (cursor.getString(1) == "-") {
                    if (!motList.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
                        motList.add(cursor.getString(R.string.sinIdioma).toUpperCase())
                    }

                } else {
                    if (!motList.contains<Any>(cursor.getString(1).toUpperCase())) {
                        motList.add(cursor.getString(1).toUpperCase())
                    }
                }
            }
        }
        cursor.close()

        val toReturn = MutableLiveData<ArrayList<String>>()
        toReturn.value = motList
        return toReturn
    }

    fun getTheMotsList(mLangue: String, context: Context): MutableLiveData<ArrayList<Mot>> {
        val mObjectReturn = MutableLiveData<ArrayList<Mot>>()
        val cursor = DataManager.createDB(context).readableDatabase.rawQuery("SELECT * FROM fillittionary WHERE idioma = '$mLangue' COLLATE NOCASE", null)

        val mList = ArrayList<Mot>()
        while (cursor.moveToNext()) {
            val mMot = Mot()
            mMot.motEn1 = cursor.getString(1)
            mMot.motEn2 = cursor.getString(2)
            mMot.tipo = cursor.getString(3)
            mMot.id = cursor.getLong(0)

            mList.add(mMot)
        }

        mObjectReturn.value = mList

        cursor.close()
        return mObjectReturn
    }

    fun getTheMotToEdit(context: Context, id: Long, newMot: Boolean): MutableLiveData<MotsList> {
        val mMotsList = MotsList()

        mMotsList.singleMot = palabra(id, context)
        mMotsList.langues = getTheLanguesToEdit(context, newMot)

        val mValueToReturn = MutableLiveData<MotsList>()
        mValueToReturn.value = mMotsList
        return mValueToReturn
    }

    fun getTheLanguesToEdit(context: Context, newMot: Boolean): ArrayList<String> {
        val cursor = wantLangue(context)
        val containsLangues = ArrayList<String>()

        while (cursor.moveToNext()) {
            if (!cursor.isNull(1)) {

                if (cursor.getString(1) == "") {
                    Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                    if (!newMot)
                        DataManager.delete(cursor.getLong(0), context)

                } else if (cursor.getString(1) == "-") {
                    if (!containsLangues.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
                        containsLangues.add(cursor.getString(R.string.sinIdioma).toUpperCase())
                    }

                } else {
                    if (!containsLangues.contains<Any>(cursor.getString(1).toUpperCase())) {
                        containsLangues.add(cursor.getString(1).toUpperCase())
                    }
                }
            }
        }

        return containsLangues
    }

    fun saveTheMotToEdit(id: Long, newMot: Boolean, context: Context) {
        buildAndSaveTheMot(id, newMot, context)
    }

    fun buildAndSaveTheMot(id: Long, newMot: Boolean, context: Context) {
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

        val arl = getTheLanguesToEdit(context, newMot)
        if (arl[mActivity.spinnerLangue.selectedItemPosition] == "") {
            mot.idioma = context.getString(R.string.sinIdioma).toUpperCase()
        } else {
            mot.idioma = arl[mActivity.spinnerLangue.selectedItemPosition].toUpperCase()
        }

        mot.id = id

        updateParoles(id, mot, context)
    }

    fun getTheMotToEditForLangue(id: Long, newMot: Boolean, context: Context): MutableLiveData<MotsList> {
        val mMotsList = MotsList()

        mMotsList.singleMot = palabra(id, context)
        mMotsList.langues = getTheLanguesToEditForLangue(newMot, context)

        val mValueToReturn = MutableLiveData<MotsList>()
        mValueToReturn.value = mMotsList
        return mValueToReturn
    }

    fun getTheLanguesToEditForLangue(newMot: Boolean, context: Context): ArrayList<String> {
        val cursor = wantLangue(context)
        val containsLangues = ArrayList<String>()

        while (cursor.moveToNext()) {
            if (!cursor.isNull(1)) {

                if (cursor.getString(1) == "") {
                    Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                    if (!newMot)
                        delete(cursor.getLong(0), context)

                } else if (cursor.getString(1) == "-") {
                    if (!containsLangues.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
                        containsLangues.add(cursor.getString(R.string.sinIdioma).toUpperCase())
                    }

                } else {
                    if (!containsLangues.contains<Any>(cursor.getString(1).toUpperCase())) {
                        containsLangues.add(cursor.getString(1).toUpperCase())
                    }
                }
            }
        }

        return containsLangues
    }

    fun buildAndSaveTheMotForLangue(id: Long, context: Activity) {
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

        mot.id = id

        updateParoles(id, mot, context)
    }

    fun saveTheMotForLangue(id: Long, context: Activity) {
        buildAndSaveTheMotForLangue(id, context)
    }

    fun deleteMotForLangue(id: Long, context: Context) {
        delete(id, context)
    }
}

