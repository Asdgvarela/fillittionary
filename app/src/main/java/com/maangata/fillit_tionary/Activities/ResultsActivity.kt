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
import com.maangata.fillit_tionary.Utils.QuizHelper

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by zosdam on 16/03/16.
 */
class ResultsActivity : AppCompatActivity() {

    private lateinit var idioma: String
    private lateinit var field1: TextView
    private lateinit var field2: TextView
    private lateinit var field3: TextView
    private lateinit var field4: TextView
    private lateinit var field5: TextView
    private lateinit var buttonBack: FloatingActionButton
    private lateinit var buttonDetails: FloatingActionButton
    private lateinit var resultados: ArrayList<QuizHelper.QuizTrials>
    private var aciertos: Int = 0
    private var intentos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_layout)

        val intentBack = intent
        idioma = intentBack.getStringExtra("langue")!!
        aciertos = intentBack.getIntExtra("aciertos", 0)
        intentos = intentBack.getIntExtra("intentos", 0)
        resultados = intentBack.getParcelableArrayListExtra("aciertosString")!!

        field1 = findViewById<View>(R.id.firstResultsBox) as TextView
        field1.text =
            getString(R.string.fraseResultados) + " " + idioma + " " + getString(R.string.fraseResultados2) + ": "
        field2 = findViewById<View>(R.id.secondResultsBox) as TextView
        field2.text = "$aciertos/$intentos"
        field3 = findViewById<View>(R.id.thirdResultsBox) as TextView
        field3.text = getString(R.string.repetirQuiz)
        buttonBack = findViewById<View>(R.id.buttonBack) as FloatingActionButton
        buttonBack.setOnClickListener {
            val intBack = Intent(this@ResultsActivity, QuizMeActivity::class.java)
            intBack.putExtra(Intent.EXTRA_TEXT, idioma)
            startActivity(intBack)
            finish()
        }

        field4 = findViewById<View>(R.id.field4) as TextView
        field4.setText(R.string.preguntaResults)
        buttonDetails = findViewById<View>(R.id.buttonDetalleResults) as FloatingActionButton
        buttonDetails.setOnClickListener {
            field5 = findViewById<View>(R.id.field5) as TextView
            field5.text = getResultsInAShowableWay(resultados)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intBack = Intent(this@ResultsActivity, MainActivity::class.java)
        intBack.putExtra("string", idioma)
        startActivity(intBack)
        finish()
    }

    private fun getResultsInAShowableWay(mList: ArrayList<QuizHelper.QuizTrials>): String {
        var mResult = getString(R.string.resultados) + ":\n"
        for (trial in mList) {
            mResult += getResultFromTrial(trial)
        }
        return mResult
    }

    private fun getResultFromTrial(mTrial: QuizHelper.QuizTrials): String {
        var mWord = mTrial.trueWord + "\t\t\t" + mTrial.trueTranslation + "\t\t->  "
        mWord += if (mTrial.trueTranslation.equals(mTrial.trial)) {
            getString(R.string.acierto) + "\n"
        } else {
            getString(R.string.error) + "\n"
        }

        return mWord
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
