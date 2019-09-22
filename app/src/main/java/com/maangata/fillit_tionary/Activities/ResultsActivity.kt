package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maangata.fillit_tionary.R

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by zosdam on 16/03/16.
 */
class ResultsActivity : AppCompatActivity() {

    lateinit var idioma: String
    lateinit internal var field1: TextView
    lateinit internal var field2: TextView
    lateinit internal var field3: TextView
    lateinit internal var field4: TextView
    lateinit internal var field5: TextView
    lateinit internal var buttonBack: FloatingActionButton
    lateinit internal var buttonDetails: FloatingActionButton
    lateinit internal var resultados: String
    internal var aciertos: Int = 0
    internal var intentos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_layout)

        val intentBack = intent
        idioma = intentBack.getStringExtra("langue")
        aciertos = intentBack.getIntExtra("aciertos", 0)
        intentos = intentBack.getIntExtra("intentos", 0)
        val tanteoAciertos = intentBack.getStringExtra("aciertosString")
        resultados = arrayTanteo(tanteoAciertos)

        field1 = findViewById<View>(R.id.firstResultsBox) as TextView
        field1.text =
            getString(R.string.fraseResultados) + " " + idioma + " " + getString(R.string.fraseResultados2) + ": "
        field2 = findViewById<View>(R.id.secondResultsBox) as TextView
        field2.text = "$aciertos/$intentos"
        field3 = findViewById<View>(R.id.thirdResultsBox) as TextView
        field3.text = getString(R.string.repetirQuizz)
        buttonBack = findViewById<View>(R.id.buttonBack) as FloatingActionButton
        buttonBack.setOnClickListener {
            val intBack = Intent(this@ResultsActivity, QuizzMeActivity::class.java)
            intBack.putExtra(Intent.EXTRA_TEXT, idioma)
            startActivity(intBack)
            finish()
        }

        field4 = findViewById<View>(R.id.field4) as TextView
        field4.setText(R.string.preguntaResults)
        buttonDetails = findViewById<View>(R.id.buttonDetalleResults) as FloatingActionButton
        buttonDetails.setOnClickListener {
            field5 = findViewById<View>(R.id.field5) as TextView
            field5.text = resultados
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intBack = Intent(this@ResultsActivity, MainActivity::class.java)
        intBack.putExtra("string", idioma)
        startActivity(intBack)
        finish()
    }

    /**
     * I use it to turn the String I receive from the Quizz with the results into an ArrayList.
     * @param string Contains the String to be turned into an ArrayList.
     * @return The ArrayList.
     */
    fun arrayTanteo(string: String): String {

        val temp1 = string.split("/////".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val stringList = ArrayList(Arrays.asList(*temp1))
        var theString = getString(R.string.resultados) + ":\n"

        for (i in stringList.indices) {
            val chunks = stringList[i].split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (chunks[1].toLowerCase() == chunks[2].toLowerCase()) {
                val temp3 = chunks[0] + "\t\t\t" + chunks[1] + "\t\t->  " + getString(R.string.acierto) + "\n"
                theString = theString + temp3

            } else {
                val temp3 = chunks[0] + "\t\t\t" + chunks[1] + "\t\t->  " + getString(R.string.error) + "\n"
                theString = theString + temp3
            }
        }
        return theString
    }

    fun shareTheWord() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.compartirResultados1) + " " + idioma + " "
                    + getString(R.string.compartirResultados2) + " " + aciertos + "/" + intentos + "."
        )
        startActivity(Intent.createChooser(i, getString(R.string.compartir_con)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_results, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val idm = item.itemId


        if (idm == R.id.share_button_results) {
            shareTheWord()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
