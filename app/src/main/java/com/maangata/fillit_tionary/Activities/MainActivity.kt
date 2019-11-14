package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maangata.fillit_tionary.Adapters.AdaptadorMain
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Mvvm.AllMotsViewModel
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.RESULT_EDIT_MAIN
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import com.maangata.fillit_tionary.Utils.Constants.STRING
import com.maangata.fillit_tionary.Utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var langue: String
    var numOfWords: Int = 0
    lateinit var allMotsViewModel: AllMotsViewModel
    lateinit var mAdapter: AdaptadorMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras!!
        langue = bundle.getString(STRING, "")

        title = langue

        listView = findViewById(R.id.listview)
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> goToCloserLook(id) }

        getAllTheWords()
    }

    private fun getAllTheWords() {
        val mFactory: AllMotsViewModel.Factory = AllMotsViewModel.Factory(application, langue)
        allMotsViewModel = ViewModelProviders.of(this, mFactory).get(AllMotsViewModel::class.java)
        allMotsViewModel.mMediator.observe(this, Observer {})
        allMotsViewModel.getAllMotsViewModel().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    finish()
                } else {
                    mAdapter.setList(it)
                    setTheMotList(it)
                }
            }
        })

        mAdapter = AdaptadorMain(this)
        listView.adapter = mAdapter
    }

    private fun setTheMotList(mCursor: List<Mot>) {
        numOfWords = mCursor.size
    }

    private fun ajouterMot() {
        Utils.addWord(langue, this@MainActivity, application)
    }

    // Busca una palabara en el campo #1 y #2 de la base de datos. En Palabra y Traducción.
    private fun lanzarBuscarPalabra() {
        Utils.searchWord(this@MainActivity, application)
    }

    private fun goToCloserLook(id: Long) {
        val intent = Intent(this@MainActivity, CloserLook::class.java)
        intent.putExtra(ID, id)
        intent.putExtra(LANGUE, langue)
        startActivityForResult(intent, RESULT_EDIT_MAIN)
    }

    // Código imprescindible para que se refresque la MainActivity al volver de CloserLook. Si no no hay constancia de que
    // esta actividad tiene que recibir información de CloserLook y no se refresca.
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        allMotsViewModel.refreshViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun launchQuiz() {
        Utils.startQuiz(langue, numOfWords, this@MainActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_button -> ajouterMot()
            R.id.search_button -> lanzarBuscarPalabra()
            R.id.quizzme -> launchQuiz()
        }

        return super.onOptionsItemSelected(item)

    }
}