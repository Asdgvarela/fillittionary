package com.maangata.fillit_tionary.Room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.maangata.fillit_tionary.Model.Mot


@Dao
interface FillitDao {

    @Insert
    fun insert(vararg mot: Mot)

    @Update
    fun update(vararg mot: Mot)

    @Delete
    fun delete(mot: Mot)

    @Query("SELECT * FROM fillittionary WHERE _id = :id")
    fun getPalabra(id: Long): LiveData<Mot>

    @Query("SELECT _id FROM fillittionary WHERE palabra = :palabra COLLATE NOCASE")
    fun searchPalabra(palabra: String): Long

    @Query("SELECT _id FROM fillittionary WHERE traduccion = :palabra COLLATE NOCASE")
    fun searchTraduccion(palabra: String): Long

//    @Query("SELECT _id, idioma FROM fillittionary")
//    fun getLangues(): List<Pair<Long, String>>

    @Query("SELECT * FROM fillittionary")
    fun getLanguesMots(): LiveData<List<Mot>>

    @Query("SELECT * FROM fillittionary WHERE idioma = :mLangue COLLATE NOCASE")
    fun searchByLangue(mLangue: String): LiveData<List<Mot>>

    @Query("SELECT _id FROM fillittionary WHERE flag = 'newMot' COLLATE NOCASE")
    fun getIdFromNewWord(): Long

}