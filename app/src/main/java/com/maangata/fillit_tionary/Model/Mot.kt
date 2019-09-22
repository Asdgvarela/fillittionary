package com.maangata.fillit_tionary.Model

/**
 * Created by zosdam on 1/09/15.
 */
class Mot {

    var motEn1: String
    var motEn2: String
    var tipo: String
    var nota: String
    lateinit var idioma: String
    lateinit var foto: String
    lateinit var sonido: String

    constructor() {
        this.motEn1 = ""
        this.motEn2 = ""
        this.tipo = ""
        this.nota = ""
        this.idioma = ""
        this.foto = ""
        this.sonido = ""
    }

    constructor(motEn1: String, motEn2: String, tipo: String, nota: String, idioma: String) {

        this.motEn1 = motEn1
        this.motEn2 = motEn2
        this.tipo = tipo
        this.nota = nota
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
                '}'.toString()
    }

}