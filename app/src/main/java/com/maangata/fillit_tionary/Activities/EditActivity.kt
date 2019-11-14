package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.Mvvm.MotToEditViewModel
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import com.maangata.fillit_tionary.Utils.Constants.NUEVO

/**
 * Created by zosdam on 3/09/15.
 */

class EditActivity : AppCompatActivity() {

    lateinit var motEn1E: EditText
    lateinit var motEn2E: EditText
    lateinit var tipoE: EditText
    lateinit var notaE: EditText
    var id: Long = 0
    lateinit var spinnerLangue: Spinner
    lateinit var langue: String
    lateinit var sound: String
    var newWord: Boolean = false
    lateinit var mMotToEditViewModel: MotToEditViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editing)

        val extras = intent.extras
        id = extras!!.getLong(ID, -1)
        langue = extras.getString(LANGUE)!!
        newWord = extras.getBoolean(NUEVO)

        getTheMot(id, newWord)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getTheMot(id: Long, newWord: Boolean) {
        motEn1E = findViewById(R.id.moten1edit)
        motEn2E = findViewById(R.id.moten2edit)
        tipoE = findViewById(R.id.tipoedit)
        notaE = findViewById(R.id.notaedit)
        spinnerLangue = findViewById<View>(R.id.spinnerLangue) as Spinner

        val mFactory = MotToEditViewModel.Factory(this@EditActivity, id, newWord, application)
        mMotToEditViewModel = ViewModelProviders.of(this@EditActivity, mFactory).get(MotToEditViewModel::class.java)
        mMotToEditViewModel.mMediatorLiveData.observe(this@EditActivity, Observer {})
        mMotToEditViewModel.mMotToEditViewModel.observe(this@EditActivity, Observer {
            if (it != null) {
                sound = it.singleMot.sonido!!
                if (it.langues.isNotEmpty()) {
                    setTheMots(it)
                }
            }
        })
    }

    private fun setTheMots(mMotsList: MotsList) {
        motEn1E.setText(mMotsList.singleMot.motEn1)
        motEn2E.setText(mMotsList.singleMot.motEn2)
        tipoE.setText(mMotsList.singleMot.tipo)
        notaE.setText(mMotsList.singleMot.nota)

        spinnerLangue = findViewById<View>(R.id.spinnerLangue) as Spinner
        val spinAdapt = ArrayAdapter(this, android.R.layout.simple_spinner_item, mMotsList.langues)
        spinAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLangue.adapter = spinAdapt

        val setLang = mMotsList.singleMot.idioma!!.toUpperCase()
        when (setLang) {
            "" -> {
                val spinnerPos = mMotsList.langues.lastIndexOf(langue)
                spinnerLangue.setSelection(spinnerPos)

            }
            "-" -> {
                val spinnerPos = mMotsList.langues.lastIndexOf(langue)
                spinnerLangue.setSelection(spinnerPos)

            }
            else -> {
                val spinnerPos = mMotsList.langues.lastIndexOf(langue)
                spinnerLangue.setSelection(spinnerPos)
            }
        }

        spinnerLangue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mMotsList.langues[spinnerLangue.selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        if (intent.extras!!.getBoolean(NUEVO, false)) {
            mMotToEditViewModel.deleteMot(mMotToEditViewModel.mMotToEditViewModel.value!!.singleMot)
        }
        finish()
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_editing, menu)
        return true
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val idm = item.itemId

        if (idm == R.id.acceptedit) {
            saveTheMot(sound)

            return true
        }

        if (idm == R.id.canceledit) {
            if (intent.extras!!.getBoolean(NUEVO, false)) {
                deleteMot()
            } else {
                finish()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun saveTheMot(sound: String) {
        mMotToEditViewModel.saveTheMotToEdit(sound)
        finish()
    }

    private fun deleteMot() {
        mMotToEditViewModel.deleteMot(mMotToEditViewModel.mMotToEditViewModel.value!!.singleMot)
        finish()
    }
}
