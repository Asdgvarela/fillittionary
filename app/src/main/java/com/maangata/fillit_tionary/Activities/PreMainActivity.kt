package com.maangata.fillit_tionary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maangata.fillit_tionary.Adapters.AdaptadorCursorPRE
import com.maangata.fillit_tionary.Mvvm.LanguesViewModel
import com.maangata.fillit_tionary.R
import java.util.ArrayList
import com.maangata.fillit_tionary.Utils.Constants.TOTHELIST
import com.maangata.fillit_tionary.Utils.Constants.STRING
import com.maangata.fillit_tionary.Utils.Utils

/**
 * Created by zosdam on 18/12/15.
 */
class PreMainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var langueViewModel: LanguesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.idiomas).toUpperCase()

        listView = findViewById<View>(R.id.listview) as ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@PreMainActivity, MainActivity::class.java)
            intent.putExtra(STRING, parent.adapter.getItem(position).toString())

            startActivityForResult(intent, TOTHELIST)
        }

        iniciaLangues()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun iniciaLangues() {
        langueViewModel = ViewModelProviders.of(this).get(LanguesViewModel::class.java)
        langueViewModel.getTheViewModel().observe(this, Observer<ArrayList<String>> {
            if (it != null) {
                (listView.adapter as AdaptadorCursorPRE).arl = it
                (listView.adapter as AdaptadorCursorPRE).notifyDataSetChanged()
            }
        })

        val mAdapter = AdaptadorCursorPRE(this@PreMainActivity, R.layout.elemento_pre_lista, R.id.textLang, langueViewModel.getTheViewModel().value!!)
        listView.adapter = mAdapter
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        iniciaLangues()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun ajouterMotForLangue() {
        Utils.addWordForLanguage(this@PreMainActivity)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun lanzarBuscarPalabra() {
        Utils.searchWord(this@PreMainActivity)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun exportDB(): Boolean {
        return Utils.exportData(this@PreMainActivity)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun importDB() {
        Utils.importData(this@PreMainActivity)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun emailDB() {
        Utils.emailDb(this@PreMainActivity)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun launchHelp() {
        val i = Intent(this@PreMainActivity, Help::class.java)
        startActivity(i)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_premain, menu)
        return true
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.new_langue -> ajouterMotForLangue()
            R.id.search_button -> lanzarBuscarPalabra()
            R.id.export_button -> exportDB()
            R.id.import_button -> importDB()
            R.id.help_button -> launchHelp()
            R.id.send_button -> emailDB()
        }
        return super.onOptionsItemSelected(item)
    }
}
