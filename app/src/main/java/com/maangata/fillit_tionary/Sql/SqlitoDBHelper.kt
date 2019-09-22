package com.maangata.fillit_tionary.Sql

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by zosdam on 1/09/15.
 */
class SqlitoDBHelper
/**
 * Creo el espacio para la base de datos, que desciende de SQLite y tal.
 * Le meto el contexto, y uso el constructor padre.
 * Se pone el contexto, el nombre de la base de datos, un null por no se que parametro y el numero de version
 * de la base de datos.
 * @param contexto
 */

/**
 * The version 2, here in the code, comes with the adding of the column called IDIOMA.
 * Version 1 didnt have any upgrade.
 * @param contexto
 */
    (contexto: Context)
/** This the DB version number */
    : SQLiteOpenHelper(contexto, "fillittionary", null, 3) {

    /**
     * Creo la base de datos propiamente dicha. Con sus columnas, el tipo de datos que alberga y tal.
     * La primera columna se tiene que llamar _id, pues albergara el id de esa entrada. ES IMPORTANTISIMO. El tipo de dato tb
     * tiene que ser el que aparece ahi.
     *
     * Como curiosidad, he añadido un campo extra, llamado flag, para poder manejar con eficiencia y elegancia los eventos en
     * los que introduzco nuevos datos. De tal manera se puede ir a EditActivity y que aparezca limpia.
     * El usuario nunca se dara cuenta de que hay ese campo.
     * @param db
     */

    // In case I added more fields to the DB, I have to REMEMBER THAT IN PREMAINACTIVITY I SHOULD CHANGE THE OF COLUMNS THE CURSOR HAS. I the method IniciaLangues().

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE fillittionary (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "palabra TEXT, " +
                    "traduccion TEXT, " +
                    "funcion TEXT, " +
                    "nota TEXT, " +
                    "flag TEXT, " +
                    "idioma TEXT, " +
                    "foto TEXT, " +
                    "sonido TEXT)"
        )
        db.execSQL(
            "INSERT INTO fillittionary VALUES (null, " +
                    "'Ejemplo', " + "'Example', " + "'F', " + "'Noun', " + "'', " + "'English', " + "'', '')"
        )

    }

    /**
     * MUy importante cuando se realicen cambios en la estructura de la DB. Ojo al valor por defecto que se pone a la nueva columna.
     * Si no, a la hora de importar, dará problemas, y a la hora de leerla quizá tb. Mejor dejarlo tod cerrado y hecho.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // Gracias a añadir un campo por defecto (DEFAULT '-') a la alteración de la DB se puede realizar la actualización
        // de la DB de manera apropiada y sin fallos.
        // De lo contrario, al no tener el campo añadido, IDIOMA, un valor, da errores de tipo NullPointException, dado que
        // el cursor creado no encuentra valor al buscar en ese campo. Poniéndole un valor por defecto el error desaparece
        // y tod funciona tan bien como siempre.
        // Aparece el -. Lo ideal sería que saliera INDEFINIDO, pero al no ser actividad resulta imposible (CREO).
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE fillittionary ADD COLUMN idioma TEXT DEFAULT '-'")
            db.execSQL("ALTER TABLE fillittionary ADD COLUMN foto TEXT DEFAULT ''")
            db.execSQL("ALTER TABLE fillittionary ADD COLUMN sonido TEXT DEFAULT ''")

        } else if (oldVersion == 2) {
            db.execSQL("ALTER TABLE fillittionary ADD COLUMN foto TEXT DEFAULT ''")
            db.execSQL("ALTER TABLE fillittionary ADD COLUMN sonido TEXT DEFAULT ''")

        }

    }
}
