package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Mvp.MotToEditForLangueModelImpl
import com.maangata.fillit_tionary.Mvp.MotToEditModelImpl
import com.maangata.fillit_tionary.Mvp.MotToEditPresenterImpl
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import com.maangata.fillit_tionary.Utils.Constants.NUEVO

/**
 * Created by zosdam on 26/12/15.
 */

/**
 * Este método es como el EditActivity. Particularidades comentadas.
 */
class EditActivityForLangue : AppCompatActivity(), MainContract.MotToEdit.ViewCallBack {
    override fun setTheMot(mMotsList: MotsList) {
    }

    override fun onFinishedSaving() {
        finish()
    }

    override fun onFinishedDeleting() {
        finish()
    }

    lateinit var langue: String
    lateinit var motEn1E: EditText
    lateinit var motEn2E: EditText
    lateinit var tipoE: EditText
    lateinit var notaE: EditText
    lateinit var idioma: EditText
    var id: Long = 0
    var newWord = false
    lateinit var mPresenter: MotToEditPresenterImpl

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editing_langue)

        val extras = intent.extras!!
        id = extras.getLong(ID, -1)
//        mot = DataManager.palabra(id, this)
        newWord = extras.getBoolean(NUEVO)
        langue = extras.getString(LANGUE, "")
        mPresenter = MotToEditPresenterImpl(this, MotToEditForLangueModelImpl(this@EditActivityForLangue))
        
        setTheMot()
    }

    fun setTheMot() {
        motEn1E = findViewById<View>(R.id.moten1edit) as EditText

        motEn2E = findViewById<View>(R.id.moten2edit) as EditText

        tipoE = findViewById<View>(R.id.tipoedit) as EditText

        notaE = findViewById<View>(R.id.notaedit) as EditText

        idioma = findViewById<View>(R.id.idiomaedit) as EditText
        idioma.setText(langue)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() {
        if (newWord) {
            mPresenter.OnRequestDelete(id)
        } else {
            finish()
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_editing, menu)
        return true
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.acceptedit -> {
                mPresenter.OnRequestSaveMot(id, newWord)
                return true
            }

            R.id.canceledit -> {
                if (newWord) {
                    mPresenter.OnRequestDelete(id)
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
