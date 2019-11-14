package com.maangata.fillit_tionary.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Mvvm.CloserLookViewModel
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import java.io.File
import java.io.IOException
import com.maangata.fillit_tionary.Utils.Constants.MY_PERMISSIONS_REQUEST
import com.maangata.fillit_tionary.Utils.Constants.RESULT_EDIT

/**
 * Created by zosdam on 3/09/15.
 */
class CloserLook : AppCompatActivity() {

    lateinit var motEn1: TextView
    lateinit var motEn2: TextView
    lateinit var tipo: TextView
    lateinit var nota: TextView
    private var id: Long = 0
    lateinit var botonGrabar: FloatingActionButton
    lateinit var botonPlay: FloatingActionButton
    lateinit var botonStop: FloatingActionButton
    lateinit var botonQuitarSonido: FloatingActionButton
    lateinit var langue: String
    var soundPath: String = (Environment.getExternalStorageDirectory().toString() + File.separator + "Fillit_tionary" +
            File.separator + "sound" + File.separator)
    lateinit var record: MediaRecorder
    lateinit var toPlay: MediaPlayer
    private var isRecording: Boolean = false
    var mMot: Mot = Mot()
    lateinit var mCloserLookViewModel: CloserLookViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closer_look)

        val bundle = intent.extras!!
        id = bundle.getLong(ID)
        langue = bundle.getString(LANGUE, "")

        motEn1 = findViewById<View>(R.id.moten1closer) as TextView
        motEn2 = findViewById<View>(R.id.moten2closer) as TextView
        tipo = findViewById<View>(R.id.tipocloser) as TextView
        nota = findViewById<View>(R.id.notacloser) as TextView
        nota.setTypeface(null, Typeface.ITALIC)
        botonGrabar = findViewById<View>(R.id.record_button_fab) as FloatingActionButton
        botonPlay = findViewById<View>(R.id.play_button_fab) as FloatingActionButton
        botonPlay.hide()
        botonStop = findViewById<View>(R.id.stop_button_fab) as FloatingActionButton
        botonStop.hide()
        botonQuitarSonido = findViewById<View>(R.id.botonQuitarSonido) as FloatingActionButton
        botonQuitarSonido.hide()
        if (!hasMicrophone()) {
            botonPlay.isEnabled = false
            botonStop.isEnabled = false
            botonGrabar.isEnabled = false
        } else {
            botonStop.isEnabled = false
            botonGrabar.isEnabled = true
        }

        updateVistas()
    }

    private fun updateVistas() {
        val mFactory = CloserLookViewModel.Factory(application, id)
        mCloserLookViewModel = ViewModelProviders.of(this, mFactory).get(CloserLookViewModel::class.java)
        mCloserLookViewModel.mMediatorLiveData.observe(this, Observer {})
        mCloserLookViewModel.mCloserLookViewModel.observe(this, Observer {
            if (it != null) {
                mMot = it
                setTheViews(it)
            }
        })
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun hasMicrophone(): Boolean {
        val pmanager = this.packageManager
        return pmanager.hasSystemFeature(
            PackageManager.FEATURE_MICROPHONE
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setTheViews(mot: Mot) {
        motEn1.text = mot.motEn1
        motEn2.text = mot.motEn2
        tipo.text = mot.tipo
        nota.text = mot.nota
        if (mot.sonido == "") {
            botonPlay.hide()
            botonQuitarSonido.hide()
        } else {
            botonPlay.show()
            botonPlay.isEnabled = true
            botonQuitarSonido.show()
        }
        val soundPath = Environment.getExternalStorageDirectory().toString() + File.separator + "Fillit_tionary" +
                File.separator + "sound" + File.separator + "sound_" + mot.motEn2 + ".3gp"
        botonPlay.setOnClickListener {
            val tempFile = File(soundPath)
            if (tempFile.exists()) {
                playSound(mot.sonido!!)
            }
        }
        botonGrabar.setOnClickListener { recordAudio() }
        botonQuitarSonido.setOnClickListener { quitarSonido() }
        botonStop.setOnClickListener { stopRecording() }
        botonStop.hide()
        isRecording = false
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun clickEdit() {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(ID, id)
        intent.putExtra(LANGUE, langue)
        startActivityForResult(intent, RESULT_EDIT)
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_EDIT) {
            mCloserLookViewModel.refreshViewModel()
        }
    }

    private fun quitarSonido() {
        botonPlay.hide()
        botonStop.hide()
        botonQuitarSonido.hide()

        Toast.makeText(this@CloserLook, R.string.sonidoEliminado, Toast.LENGTH_SHORT).show()

        mCloserLookViewModel.removeSound()
    }

    private fun playSound(mSound: String) {

        botonGrabar.isEnabled = false
        botonPlay.isEnabled = false
        botonStop.setEnabled(true)

        botonStop.show()

        toPlay = MediaPlayer()

        if (mSound != "" || mSound != "null") {
            try {

                if (mSound == getString(R.string.sonido).toUpperCase()) {
                    Toast.makeText(this@CloserLook, R.string.sonidoInvalido, Toast.LENGTH_SHORT).show()
                } else {
                    toPlay.setDataSource(mSound)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                toPlay.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            toPlay.start()

            Toast.makeText(this@CloserLook, R.string.reproduciendo_sonido, Toast.LENGTH_SHORT).show()

            toPlay.setOnCompletionListener {
                botonStop.hide()
                botonGrabar.isEnabled = true
                botonPlay.isEnabled = true
            }
        }
    }

    private fun stopRecording() {
        botonStop.isEnabled = false

        if (isRecording) {
            record.stop()
            record.release()
            isRecording = false

            botonGrabar.isEnabled = true
            botonPlay.isEnabled = true
            botonQuitarSonido.isEnabled = true

            botonPlay.show()
            botonStop.hide()
            botonQuitarSonido.show()

            Toast.makeText(this@CloserLook, R.string.grabacion_parada, Toast.LENGTH_SHORT).show()

            mCloserLookViewModel.updateMot()

        } else {
            toPlay.release()
            botonGrabar.isEnabled = true
            botonPlay.isEnabled = true

            botonStop.hide()
            botonPlay.show()
            botonQuitarSonido.show()

            Toast.makeText(this@CloserLook, R.string.reproduccion_interrumpida, Toast.LENGTH_SHORT).show()
        }
    }

    private fun recordAudio() {
        val forSound = File(soundPath)
        if (!forSound.exists()) {
            forSound.mkdirs()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mListPermissions = arrayOfNulls<String>(2)
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ) {
                mListPermissions[0] = Manifest.permission.RECORD_AUDIO
                mListPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE
                requestPermissions(mListPermissions, MY_PERMISSIONS_REQUEST)
            } else {
                recordingAudio()
            }
        }
    }

    private fun recordingAudio() {
        isRecording = true
        botonStop.isEnabled = true
        botonPlay.isEnabled = false
        botonGrabar.isEnabled = false
        botonQuitarSonido.isEnabled = false

        botonStop.show()
        botonPlay.hide()

        record = MediaRecorder()
        record.setAudioSource(MediaRecorder.AudioSource.MIC)
        record.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val recordedWord = (soundPath + "sound_" + motEn2.text.toString() + ".3gp")
        record.setOutputFile(recordedWord)

        try {
            record.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        record.start()
        mCloserLookViewModel.setTheSound(recordedWord)

        Toast.makeText(this@CloserLook, R.string.grabando_audio, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_closelook, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_button -> {
                clickEdit()
                return true
            }
            R.id.delete_button -> {
                mCloserLookViewModel.deleteMot(mMot)
                finish()
                return true
            }
            R.id.share_button -> {
                shareTheWord()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    recordingAudio()
                } else {
                    Toast.makeText(this, getResources().getString(R.string.no_record_allowed), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun shareTheWord() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(
            Intent.EXTRA_TEXT,
            (getString(R.string.comparto_1) + " " + motEn1.text.toString() + " - " + motEn2.text.toString() + ". " +
                    getString(R.string.comparto_2))
        )
        startActivity(Intent.createChooser(i, getString(R.string.compartir_con)))
    }
}