package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Sql.SqlitoDBHelper

import java.util.ArrayList
import java.util.Random

/**
 * Created by zosdam on 14/03/16.
 */

class QuizzMeActivity : AppCompatActivity() {

    lateinit var sqlitodb: SqlitoDBHelper
    lateinit var langueFromMain: String
    lateinit var cursorQuizz: Cursor
    lateinit var quizzBox: TextView
    lateinit var resultsBox: TextView
    lateinit var needAHint: TextView
    lateinit var theHint: TextView
    lateinit var editAnswer: EditText
    lateinit var answerButton: FloatingActionButton
    lateinit var buttonHint: FloatingActionButton
    internal var aciertos: Int = 0
    internal var i: Int = 0
    internal var noItems: Int = 0
    internal var noPregs: Int = 0
    lateinit internal var chunks: Array<String>
    lateinit internal var aciertosString: String
    lateinit internal var theArray: ArrayList<String>
    lateinit internal var intArray: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizzme)

        sqlitodb = DataManager.createDB(this)
        val intent = intent
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            langueFromMain = intent.getStringExtra(Intent.EXTRA_TEXT)
            noPregs = intent.getIntExtra("noPregs", -1)
        }

        cursorQuizz = searchByLangue(langueFromMain)
        /** Este TextView lo quitaré, ahora es sólo para atestiguar que va bien.
         * TextView langueBox = (TextView) findViewById(R.id.idioma_quizz);
         * langueBox.setText(langueFromMain);  */
        quizzBox = findViewById<View>(R.id.box_quizz) as TextView
        editAnswer = findViewById<View>(R.id.editAnswer) as EditText
        editAnswer.setHint(R.string.quizzHint)
        answerButton = findViewById<View>(R.id.okButtonQuizz) as FloatingActionButton
        resultsBox = findViewById<View>(R.id.counterQuizz) as TextView
        resultsBox.text = getString(R.string.resultados) + ": --."
        needAHint = findViewById<View>(R.id.needAHint) as TextView
        needAHint.setText(R.string.necesitasPista)
        buttonHint = findViewById<View>(R.id.buttonHint) as FloatingActionButton
        theHint = findViewById<View>(R.id.theHint) as TextView
        theHint.text = ""


        // Inicializo las variables.
        theArray = preparingTheCursor(cursorQuizz)
        noItems = theArray.size
        if (noItems >= 10) {
            noItems = noPregs
        }

        i = 0
        aciertos = 0
        intArray = ArrayList()
        aciertosString = ""

        // Genero el primer número Random, y lo añado al array donde iré guardando todos los número que van saliendo para que no
        // se repitan
        val iRnd = Random()
        val pos = iRnd.nextInt(theArray.size)
        intArray.add(pos)

        // Cojo la primera línea del array que contiene la información del cursor y la separo en las palabras.
        chunks = theArray[pos].split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // Pongo la primera palabra en el TextView de la pantalla.
        quizzBox.text = chunks[0]
        buttonHint.setOnClickListener { theHint.text = chunks[3] }

        /**
         * Algoritmo que marca la dinámica del QUIZZ.
         * En función de acertar o no la palabra se hace una cosa u otra.
         * Siempre se suma en aciertos e i.
         * Según el número de intento se va directamente a resultados, o se sigue con el quizz
         */

        answerButton.setOnClickListener {
            if (editAnswer.text.toString().toLowerCase() == chunks[1].toLowerCase()) {
                // En este primer caso editAnswer nunca estará vacío, por lo culá es un error encapsular esa posibilidad.

                if (i == noItems - 1) {
                    i++
                    aciertos++

                    // Using five / (/////) to separate the differentes items in the String, so that then it can be parsed in the
                    // results activity.
                    val tempString = chunks[0] + "\t" + editAnswer.text + "\t" + chunks[1] + "/////"
                    aciertosString = aciertosString.replace("\n", "") + tempString.replace("\n", "")

                    val intentQuizz = Intent(this@QuizzMeActivity, ResultsActivity::class.java)
                    intentQuizz.putExtra("langue", langueFromMain)
                    intentQuizz.putExtra("aciertos", aciertos)
                    intentQuizz.putExtra("intentos", i)
                    intentQuizz.putExtra("aciertosString", aciertosString)
                    startActivity(intentQuizz)
                    finish()

                } else {
                    aciertos++
                    i++
                    // Con esto pongo cómo va el tanteo en su TextView
                    val temportal = getString(R.string.resultados) + ": " + aciertos + "/" + i + "."
                    resultsBox.text = temportal
                    // Con esto voy guardando el resulta

                    val tempString = chunks[0] + "\t" + editAnswer.text + "\t" + chunks[1] + "/////"
                    aciertosString = aciertosString.replace("\n", "") + tempString.replace("\n", "")

                    // Borro el EditText
                    editAnswer.setText("")
                    if (theHint.text.toString() != "") {
                        theHint.text = ""
                    }

                    //Vuelvo a generar un número aleatorio, con cuidado de que no se repita: para eso el WHILE.
                    var iRnd = Random()
                    var pos = iRnd.nextInt(theArray.size)
                    while (intArray.contains(pos)) {
                        iRnd = Random()
                        pos = iRnd.nextInt(theArray.size)
                    }

                    // Añado el nuevo número aleatorio, cojo un nuevo elemento del ArrayList que contiene el cursor,
                    // y pongo la nueva palabra en el TextView.
                    intArray.add(pos)
                    chunks = theArray[pos].split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    quizzBox.text = chunks[0]
                }

            } else {

                if (i == noItems - 1) {
                    i++
                    if (editAnswer.text.toString() == "") {
                        val tempString = chunks[0] + "\t" + "###" + "\t" + chunks[1] + "/////"
                        aciertosString = aciertosString + tempString
                    } else {
                        val tempString = chunks[0] + "\t" + editAnswer.text + "\t" + chunks[1] + "/////"
                        aciertosString = aciertosString.replace("\n", "") + tempString.replace("\n", "")
                    }

                    val intentQuizz = Intent(this@QuizzMeActivity, ResultsActivity::class.java)
                    intentQuizz.putExtra("langue", langueFromMain)
                    intentQuizz.putExtra("aciertos", aciertos)
                    intentQuizz.putExtra("intentos", i)
                    intentQuizz.putExtra("aciertosString", aciertosString)
                    startActivity(intentQuizz)
                    finish()

                } else {
                    i++
                    val temporal = getString(R.string.resultados) + ": " + aciertos + "/" + i + "."
                    resultsBox.text = temporal

                    if (editAnswer.text.toString() == "") {
                        val tempString = chunks[0] + "\t" + "###" + "\t" + chunks[1] + "/////"
                        aciertosString = aciertosString.replace("\n", "") + tempString.replace("\n", "")
                    } else {
                        val tempString = chunks[0] + "\t" + editAnswer.text + "\t" + chunks[1] + "/////"
                        aciertosString = aciertosString.replace("\n", "") + tempString.replace("\n", "")
                    }
                    editAnswer.setText("")
                    if (theHint.text.toString() != "") {
                        theHint.text = ""
                    }

                    var iRnd = Random()
                    var pos = iRnd.nextInt(theArray.size)

                    while (intArray.contains(pos)) {
                        iRnd = Random()
                        pos = iRnd.nextInt(theArray.size)
                    }

                    intArray.add(pos)
                    chunks = theArray[pos].split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    quizzBox.text = chunks[0]

                }
            }
        }

        cursorQuizz.close()
        sqlitodb.close()

    }

    fun searchByLangue(string: String): Cursor {
        val db = sqlitodb.readableDatabase
        return db.rawQuery("SELECT * FROM fillittionary WHERE idioma = '$string' COLLATE NOCASE", null)
    }


    fun preparingTheCursor(cursor: Cursor): ArrayList<String> {
        val array = ArrayList<String>()

        while (cursor.moveToNext()) {

            array.add(
                cursor.getString(1) + "\t" +
                        cursor.getString(2) + "\t" +
                        cursor.getString(3) + "\t" +
                        cursor.getString(4) + "\n"
            )
        }

        return array
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intBack = Intent(this@QuizzMeActivity, MainActivity::class.java)
        intBack.putExtra("string", langueFromMain)
        startActivity(intBack)
        finish()
    }
}

