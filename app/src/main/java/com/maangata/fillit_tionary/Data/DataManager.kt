package com.maangata.fillit_tionary.Data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.Sql.SqlitoDBHelper

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
        }
        cursor.close()
        db.close()
        return mot
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
            motsList.motsList.add(mot)
        }

        return motsList
    }
}

