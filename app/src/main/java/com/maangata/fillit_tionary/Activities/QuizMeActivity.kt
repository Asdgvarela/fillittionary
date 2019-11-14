package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maangata.fillit_tionary.Mvvm.QuizViewModel
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.QuizHelper

/**
 * Created by zosdam on 14/03/16.
 */

class QuizMeActivity : AppCompatActivity() {

    private lateinit var langueFromMain: String
    private lateinit var quizzBox: TextView
    private lateinit var resultsBox: TextView
    private lateinit var needAHint: TextView
    private lateinit var theHint: TextView
    private lateinit var editAnswer: EditText
    private lateinit var answerButton: FloatingActionButton
    private lateinit var buttonHint: FloatingActionButton
    private lateinit var mQuizVm: QuizViewModel
    private lateinit var mQuizHelper: QuizHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizzme)

        val intent = intent
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            langueFromMain = intent.getStringExtra(Intent.EXTRA_TEXT)!!
        }

        quizzBox = findViewById<View>(R.id.box_quizz) as TextView
        editAnswer = findViewById<View>(R.id.editAnswer) as EditText
        editAnswer.setHint(R.string.quizzHint)
        answerButton = findViewById<View>(R.id.okButtonQuiz) as FloatingActionButton
        resultsBox = findViewById<View>(R.id.counterQuiz) as TextView
        resultsBox.text = getString(R.string.resultados) + ": --."
        needAHint = findViewById<View>(R.id.needAHint) as TextView
        needAHint.setText(R.string.necesitasPista)
        buttonHint = findViewById<View>(R.id.buttonHint) as FloatingActionButton
        theHint = findViewById<View>(R.id.theHint) as TextView
        theHint.text = ""

        val mFactory = QuizViewModel.Factory(langueFromMain, application)
        mQuizVm = ViewModelProviders.of(this, mFactory).get(QuizViewModel::class.java)
        mQuizVm.mInitialLiveData.observe(this, Observer {
            if (it != null) {
                mQuizHelper = QuizHelper(this, it, intent.getIntExtra("noPregs", -1))
                quizzBox.text = mQuizHelper.currentMot.motEn1

            }
        })

        buttonHint.setOnClickListener {
            theHint.text = mQuizHelper.currentMot.nota
        }

        answerButton.setOnClickListener {
            if (!mQuizHelper.moveToNext(editAnswer.text.toString().toLowerCase())) {
                wasLastQuestion()
            } else {
                quizzBox.text = mQuizHelper.currentMot.motEn1
                editAnswer.setText("")
                resultsBox.text = getString(R.string.resultados) +
                        ": " + mQuizHelper.getCorrectAnswers().toString() +
                        "/" + mQuizHelper.noItems + "."
            }

        }
    }

    private fun wasLastQuestion() {
        val intentQuiz = Intent(this@QuizMeActivity, ResultsActivity::class.java)
        intentQuiz.putExtra("langue", langueFromMain)
        intentQuiz.putExtra("aciertos", mQuizHelper.getCorrectAnswers())
        intentQuiz.putExtra("intentos", mQuizHelper.getIterations())
        intentQuiz.putExtra("aciertosString", mQuizHelper.getTrialsList())
        startActivity(intentQuiz)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intBack = Intent(this@QuizMeActivity, MainActivity::class.java)
        intBack.putExtra("string", langueFromMain)
        startActivity(intBack)
        finish()
    }
}

