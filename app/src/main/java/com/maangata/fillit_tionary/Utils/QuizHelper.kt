package com.maangata.fillit_tionary.Utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.maangata.fillit_tionary.Model.Mot
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

class QuizHelper(val context: Context, var motsList: List<Mot>, noQuestions: Int) {

    private var iterations = 0
    var noItems = 0
    var positionsArray = ArrayList<Int>()
    private var correctAnswer = 0
    private var correctAnswersArray = ArrayList<Mot>()
    var currentMot: Mot
    private var mListTrials = ArrayList<QuizTrials>()

    init {
        noItems = if (motsList.size > 10) {
            noQuestions
        } else {
            motsList.size
        }

        currentMot = motsList[randomizeFollowingWord()]
    }

    /**
     * Returns FALSE if its the last word to ask.
     */
    fun moveToNext(answer: String): Boolean {
        if (answer.toLowerCase().equals(currentMot.motEn2!!.toLowerCase())) {
            correctAnswer++
            correctAnswersArray.add(currentMot)
        }

        addWordToTrialsArray(answer)

        iterations++

        return if (iterations == noItems ) {
            false
        } else {
            currentMot = getWordRandomized()
            true
        }
    }

    private fun randomizeFollowingWord(): Int {
        val iRnd = Random()
        return iRnd.nextInt(noItems)
    }

    private fun getWordRandomized(): Mot {
        var mPos = randomizeFollowingWord()
        while (positionsArray.contains(mPos)) {
            mPos = randomizeFollowingWord()
        }
        positionsArray.add(mPos)
        return motsList[mPos]
    }

    private fun addWordToTrialsArray(answer: String) {
        val mTrial = QuizTrials().apply {
            trueWord = currentMot.motEn1!!
            trueTranslation = currentMot.motEn2!!
            trial = answer
        }
        mListTrials.add(mTrial)
    }

    fun getCorrectAnswers(): Int {
        return correctAnswer
    }

    fun getIterations(): Int {
        return iterations
    }

    internal fun getTrialsList(): ArrayList<QuizTrials> {
        return mListTrials
    }

    @Parcelize
    data class QuizTrials(var trueWord: String = "", var trueTranslation: String = "", var trial: String = "") : Parcelable {
    }
}