package com.maangata.fillit_tionary.Model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by zosdam on 1/09/15.
 */

// Includes the annotation for RoomDB.
@Entity(tableName = "fillittionary")
class Mot {

    @ColumnInfo(name = "palabra")
    @NonNull
    var motEn1: String

    @ColumnInfo(name = "traduccion")
    @NonNull
    var motEn2: String

    @ColumnInfo(name = "funcion")
    @NonNull
    var tipo: String

    @ColumnInfo(name = "nota")
    @NonNull
    var nota: String

    @ColumnInfo(name = "flag")
    @NonNull
    lateinit var flag: String

    @ColumnInfo(name = "idioma")
    @NonNull
    lateinit var idioma: String

    @ColumnInfo(name = "foto")
    @NonNull
    lateinit var foto: String

    @ColumnInfo(name = "sonido")
    @NonNull
    lateinit var sonido: String

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @NonNull
    var id: Long

    constructor() {
        this.motEn1 = ""
        this.motEn2 = ""
        this.tipo = ""
        this.nota = ""
        this.flag = ""
        this.idioma = ""
        this.foto = ""
        this.sonido = ""
        this.id = 0
    }

    @Ignore
    constructor(motEn1: String, motEn2: String, tipo: String, nota: String) {

        this.motEn1 = motEn1
        this.motEn2 = motEn2
        this.tipo = tipo
        this.nota = nota
        this.id = 0
    }

    /**
     * Este método en algún momento tendré que cambiarlo para ver qué imprimo, y para ponerlo en idiomas.
     * Pero será en un futuro.
     * @return
     */
    override fun toString(): String {
        return "Mots{" +
                "motEn1=" + motEn1 +
                ", motEn2=" + motEn2 +
                ", tipo=" + tipo +
                ", nota=" + nota +
                ", idioma=" + idioma +
                ", foto=" + foto +
                ", sonido=" + sonido +
                ", flag=" + flag +
                ", id=" + id +
                '}'
    }
}