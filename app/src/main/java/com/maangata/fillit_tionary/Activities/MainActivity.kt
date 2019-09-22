package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.maangata.fillit_tionary.Adapters.AdaptadorCursor
import com.maangata.fillit_tionary.Mvp.AllMotsModelCallbackImpl
import com.maangata.fillit_tionary.Mvp.AllMotsPresenterImpl
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.RESULT_EDIT_MAIN
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import com.maangata.fillit_tionary.Utils.Constants.STRING
import com.maangata.fillit_tionary.Utils.Utils
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.AllMots.ViewCallback {

    internal lateinit var listView: ListView
    internal lateinit var langue: String
    lateinit var mPresenter: AllMotsPresenterImpl
    var numOfWords: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras!!
        langue = bundle.getString(STRING, "")

        title = langue

        mPresenter = AllMotsPresenterImpl(this,  mModel = AllMotsModelCallbackImpl(this@MainActivity))

        listView = findViewById(R.id.listview)
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> goToCloserLook(id) }

        getAllTheWords()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllTheWords() {
        mPresenter.onRequestingAllMots(langue)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun setTheMotList(mCursor: Cursor) {
        numOfWords = mCursor.count
        val adapter = AdaptadorCursor(this, mCursor)
        listView.adapter = adapter
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun ajouterMot() {
        Utils.addWord(langue, this@MainActivity)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Busca una palabara en el campo #1 y #2 de la base de datos. En Palabra y Traducción.
    fun lanzarBuscarPalabra() {
        Utils.searchWord(this@MainActivity)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun goToCloserLook(id: Long) {
        val intent = Intent(this@MainActivity, CloserLook::class.java)
        intent.putExtra(ID, id)
        intent.putExtra(LANGUE, langue)
        startActivityForResult(intent, RESULT_EDIT_MAIN)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Código imprescindible para que se refresque la MainActivity al volver de CloserLook. Si no no hay constancia de que
    // esta actividad tiene que recibir información de CloserLook y no se refresca.
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getAllTheWords()
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun launchQuizz() {
        Utils.startQuizz(langue, numOfWords, this@MainActivity)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_button -> ajouterMot()
            R.id.search_button -> lanzarBuscarPalabra()
            R.id.quizzme -> launchQuizz()
        }

        return super.onOptionsItemSelected(item)

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}


/**
 * HECHOTODO: Cosas que hacer en la app.
 * - Clases que me hacen falta.
 * - MainActivity. + layout.
 * - Métodos para :
 * - Ver toda la info.
 * - Meter info en DB.
 * - Actualizar info en DB.
 * - CloserLookActivity. + layout. Poner método para poner imagen. Completar método onCreate.
 * Recordar que en el intent tendré que meter como extras toda la info de la línea de MainActivity.
 * - SQLActivity.
 * - AdaptadorCursor: para coger la info de la DB.
 * - Editar/Introducir palabra.
 * - Palabra.
 *
 */

/*

- Poder EXPORTAR la lista de palabras a un archivo de texto. Así no perderlo. HECHO.
- Poder volver a meter ese archivo en la app.
- Completar la TABLA para meter MÁS CAMPOS, para poder meter cosas útiles.
- Poder COMPARTIR con la gente lo que has aprendido ese día.
- Poder asociar una IMAGEN a la palabra.
- Grabar sonidos.
- ORDENAR según se quiera, alfabéticamente, según Funcion. HECHO.

 */