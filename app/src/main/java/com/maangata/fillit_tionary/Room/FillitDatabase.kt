package com.maangata.fillit_tionary.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maangata.fillit_tionary.Async.PopulateDb
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Utils.Constants.DB_NAME

@Database(entities =  [Mot::class], version = 1, exportSchema = false)
abstract class FillitDatabase: RoomDatabase() {
    abstract fun fillitDao(): FillitDao

    companion object {

        // The database is marked as volatile to make the object available for all the threads.
        @Volatile
        private var INSTANCE: FillitDatabase? = null

        internal fun getDatabase(context: Context): FillitDatabase? {
            if (INSTANCE == null) {
                synchronized(FillitDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<FillitDatabase>(context.applicationContext,
                            FillitDatabase::class.java, DB_NAME)
                            // This callback is used to populate the DB at the moment of creation.
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(migration_3_4)
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // We call the asynchronous task that will generate and insert the dummy data into the database.
                // This method will only be called at the time the database is created.
                PopulateDb(INSTANCE!!).execute()
            }
        }

        val migration_3_4 = object : Migration(3, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val TEMP_NAME = "fillit_temp"

                // 1. Create new table
                database.execSQL("CREATE TABLE IF NOT EXISTS `$TEMP_NAME` (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "palabra TEXT NOT NULL, " +
                        "traduccion TEXT NOT NULL, " +
                        "funcion TEXT NOT NULL, " +
                        "nota TEXT NOT NULL, " +
                        "flag TEXT NOT NULL, " +
                        "idioma TEXT NOT NULL, " +
                        "foto TEXT NOT NULL, " +
                        "sonido TEXT NOT NULL)")

               // 2. Copy the data
                database.execSQL("INSERT INTO $TEMP_NAME (_id, palabra, traduccion, funcion, nota, flag, idioma, foto, sonido) "
                        + "SELECT _id, palabra, traduccion, funcion, nota, flag, idioma, foto, sonido "
                        + "FROM $DB_NAME")

                // 3. Remove the old table
                database.execSQL("DROP TABLE $DB_NAME")

                // 4. Change the table name to the correct one
                database.execSQL("ALTER TABLE $TEMP_NAME RENAME TO $DB_NAME")

            }
        }
    }
}