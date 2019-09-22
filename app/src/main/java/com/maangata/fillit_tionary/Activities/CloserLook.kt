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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Mvp.CloserLookModelImpl
import com.maangata.fillit_tionary.Mvp.CloserLookPresenterImpl
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
class CloserLook : AppCompatActivity(), MainContract.CloserLook.ViewCallback {

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
    lateinit var mPresenter: CloserLookPresenterImpl
    var mMot: Mot = Mot()

    override fun setTheMot(mMot: Mot) {
        this.mMot = mMot
        setTheViews(mMot)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closer_look)

        val bundle = intent.extras
        id = bundle!!.getLong(ID)
        langue = bundle.getString(LANGUE, "")

        mPresenter = CloserLookPresenterImpl(this, CloserLookModelImpl(this))

        motEn1 = findViewById<View>(R.id.moten1closer) as TextView
        motEn2 = findViewById<View>(R.id.moten2closer) as TextView
        tipo = findViewById<View>(R.id.tipocloser) as TextView
        nota = findViewById<View>(R.id.notacloser) as TextView
        nota.setTypeface(null, Typeface.ITALIC)
        botonGrabar = findViewById<View>(R.id.record_button_fab) as FloatingActionButton
        botonPlay = findViewById<View>(R.id.play_button_fab) as FloatingActionButton
        botonStop = findViewById<View>(R.id.stop_button_fab) as FloatingActionButton
        botonQuitarSonido = findViewById<View>(R.id.botonQuitarSonido) as FloatingActionButton
        if (!hasMicrophone()) {
            botonPlay.isEnabled = false
            botonStop.isEnabled = false
            botonGrabar.isEnabled = false
        } else {
            botonStop.isEnabled = false
            botonGrabar.isEnabled = true
        }
        updateVistas(null)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected fun hasMicrophone(): Boolean {
        val pmanager = this.packageManager
        return pmanager.hasSystemFeature(
            PackageManager.FEATURE_MICROPHONE
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun setTheViews(mot: Mot) {
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
                playSound(mot.sonido)
            }
        }
        botonGrabar.setOnClickListener { recordAudio() }
        botonQuitarSonido.setOnClickListener {
            quitarSonido()
            botonQuitarSonido.hide()
        }
        botonStop.setOnClickListener { stopRecording(soundPath) }
        botonStop.hide()
        isRecording = false
    }

    fun updateVistas(mot: Mot?) {
        mPresenter.OnRequestMot(mot, id)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun clickEdit() {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(ID, id)
        intent.putExtra(LANGUE, langue)
        startActivityForResult(intent, RESULT_EDIT)
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_EDIT) {
            updateVistas(mMot)
        }
    }

    fun quitarSonido() {
        botonPlay.hide()
        botonStop.hide()
        botonQuitarSonido.hide()

        Toast.makeText(this@CloserLook, R.string.sonidoEliminado, Toast.LENGTH_SHORT).show()

        mPresenter.OnUpdateSound(mMot, id)
    }

    fun playSound(mSound: String) {

        botonGrabar.setEnabled(false)
        botonPlay.setEnabled(false)
        botonStop.setEnabled(true)

        botonStop.show()

        toPlay = MediaPlayer()

        if (!mSound.equals("") || !mSound.equals("null")) {
            try {

                if (mSound == getString(R.string.sonido).toUpperCase()) {
                    Toast.makeText(this@CloserLook, R.string.sonidoInvalido, Toast.LENGTH_SHORT).show()
                }
                else {
                    toPlay.setDataSource(mSound)
                }

            }
            catch (e:IOException) {
                e.printStackTrace()
            }

            try {
                toPlay.prepare()
            }
            catch (e:IOException) {
                e.printStackTrace()
            }

            toPlay.start()

            Toast.makeText(this@CloserLook, R.string.reproduciendo_sonido, Toast.LENGTH_SHORT).show()

            toPlay.setOnCompletionListener(object:MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp:MediaPlayer) {
                    botonStop.hide()
                    botonGrabar.setEnabled(true)
                    botonPlay.setEnabled(true)
                }
            })
        }
    }

    fun stopRecording(recordedWord: String) {
        botonStop.setEnabled(false)

        if (isRecording) {
            record.stop()
            record.release()
            isRecording = false

            botonGrabar.setEnabled(true)
            botonPlay.setEnabled(true)
            botonQuitarSonido.setEnabled(true)

            botonPlay.show()
            botonStop.hide()
            botonQuitarSonido.show()

            Toast.makeText(this@CloserLook, R.string.grabacion_parada, Toast.LENGTH_SHORT).show()

            mPresenter.OnUpdateSound(mMot, id)

        } else {
            toPlay.release()
            botonGrabar.setEnabled(true)
            botonPlay.setEnabled(true)

            botonStop.hide()
            botonPlay.show()
            botonQuitarSonido.show()

            Toast.makeText(this@CloserLook, R.string.reproduccion_interrumpida, Toast.LENGTH_SHORT).show()
        }
    }

    fun recordAudio() {

        val forSound = File(soundPath)
        if (!forSound.exists()) {
            forSound.mkdirs()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mListPermissions = arrayOfNulls<String>(2)
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                mListPermissions[0] = Manifest.permission.RECORD_AUDIO
                mListPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE
                requestPermissions(mListPermissions, MY_PERMISSIONS_REQUEST)
            }
            else {
                recordingAudio()
            }
        }
    }

    private fun recordingAudio() {
        isRecording = true
        botonStop.setEnabled(true)
        botonPlay.setEnabled(false)
        botonGrabar.setEnabled(false)
        botonQuitarSonido.setEnabled(false)

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
        }
        catch (e:IOException) {
            e.printStackTrace()
        }

        record.start()
        Toast.makeText(this@CloserLook, R.string.grabando_audio, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu:Menu):Boolean {
        getMenuInflater().inflate(R.menu.menu_closelook, menu)
        return true
    }

    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        when (item.getItemId()) {
            R.id.edit_button -> {
                clickEdit()
                return true
            }
            R.id.delete_button -> {
                DataManager.delete(id, this@CloserLook)
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

    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults:IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    recordingAudio()
                } else {
                    Toast.makeText(this, getResources().getString(R.string.no_record_allowed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun shareTheWord() {
        val i = Intent(Intent.ACTION_SEND)
        i.setType("text/plain")
        i.putExtra(Intent.EXTRA_TEXT, (getString(R.string.comparto_1) + " " + motEn1.text.toString() + " - " + motEn2.text.toString() + ". " +
                getString(R.string.comparto_2)))
        startActivity(Intent.createChooser(i, getString(R.string.compartir_con)))
    }
}


/**else if (requestCode == RESULT_PICT && resultCode == Activity.RESULT_OK && mot != null && uriPict != null) {
 *
 * //foto.setImageBitmap(null);
 * mot.setFoto(uriPict.toString());
 * Paroles.updateParoles(id, mot);
 * //ponerFoto(foto, mot.getFoto());
 * updateVistas(id);
 * botonVerFoto.show();
 * botonQuitarFoto.show();
 *
 * /** Guardo este código un poco por guardar, porque no me funciona, pero considero que es bueno y por eso.
 *
 * Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
 * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
 * thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
 *
 * File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "Fillit_tionary"
 * + File.separator + "vision/" + "img_" + mot.getMotEn1() + ".jpg");
 *
 * FileOutputStream fo;
 * try {
 *
 * sd.createNewFile();
 * fo = new FileOutputStream(sd);
 * fo.write(bytes.toByteArray());
 * fo.close();
 * } catch (FileNotFoundException e) {
 * e.printStackTrace();
 * } catch (IOException e) {
 * e.printStackTrace();
 * }
 *
 * foto.setImageBitmap(thumbnail);
 * mot.setFoto(thumbnail.toString());
 * Paroles.updateParoles(id, mot);
 * }  */
//}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun shareTheWord() {
val i = Intent(Intent.ACTION_SEND)
i.setType("text/plain")
i.putExtra(Intent.EXTRA_TEXT, (getString(R.string.comparto_1) + " " + mot!!.getMotEn1() + " - " + mot!!.getMotEn2() + ". " +
getString(R.string.comparto_2)))
startActivity(Intent.createChooser(i, getString(R.string.compartir_con)))
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**public void prendrePhoto() {
 * Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // Mejor que MediaStore.ACTION_IMAGE_CAPTURE
 * File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "Fillit_tionary"
 * + File.separator + "vision/" + "vision_" + mot.getMotEn1() + ".jpg");
 * uriPict = Uri.fromFile(sd);
 * intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPict);
 * startActivityForResult(intent, RESULT_PICT);
 * } */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**public void goToPict() {
 * Intent intent = new Intent(CloserLook.this, FullPictureActivity.class);
 * intent.putExtra("id", id);
 * startActivity(intent);
 * } */
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**protected void quitarFoto() {
 *
 * //foto.setImageBitmap(null);
 * mot.setFoto("");
 * Paroles.updateParoles((int) id, mot);
 * botonVerFoto.hide();
 * botonQuitarFoto.hide();
 *
 * File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "Fillit_tionary"
 * + File.separator + "vision/" + "vision_" + mot.getMotEn1() + ".jpg");
 * sd.delete();
 * //Boolean delete = filePath.delete();
 * //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(photoPath))));
 * Toast.makeText(CloserLook.this, R.string.quitarFoto, Toast.LENGTH_SHORT).show();
 * } */
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**protected void ponerFoto(ImageView imageView, String uri) {
 * if (uri.equals("")) {
 * imageView.setImageBitmap(null);
 * botonQuitarFoto.hide();
 * } else{
 * imageView.setImageURI(Uri.parse(uri));
 * botonQuitarFoto.show();
 * }
 * }
*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun recordAudio() {

val recordedWord:String? = null

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
{
val mListPermissions = arrayOfNulls<String>(2)
if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
{
mListPermissions[0] = Manifest.permission.RECORD_AUDIO
mListPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE
requestPermissions(mListPermissions, MY_PERMISSIONS_REQUEST)
}
else
{
recordingAudio()
}
}
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun stopRecording() {

botonStop.setEnabled(false)

if (isRecording!!)
{
record!!.stop()
record!!.release()
record = null
isRecording = false

botonGrabar.setEnabled(true)
botonPlay.setEnabled(true)
botonQuitarSonido.setEnabled(true)

botonPlay.show()
botonStop.hide()
botonQuitarSonido.show()

Toast.makeText(this@CloserLook, R.string.grabacion_parada, Toast.LENGTH_SHORT).show()

}
else
{
toPlay!!.release()
toPlay = null
botonGrabar.setEnabled(true)
botonPlay.setEnabled(true)

botonStop.hide()
botonPlay.show()
botonQuitarSonido.show()

Toast.makeText(this@CloserLook, R.string.reproduccion_interrumpida, Toast.LENGTH_SHORT).show()
}

}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun playSound() {

botonGrabar.setEnabled(false)
botonPlay.setEnabled(false)
botonStop.setEnabled(true)

botonStop.show()

toPlay = MediaPlayer()

if (mot!!.getSonido() != "null")
{
try
{

if (mot!!.getSonido() == getString(R.string.sonido).toUpperCase())
{
Toast.makeText(this@CloserLook, R.string.sonidoInvalido, Toast.LENGTH_SHORT).show()
}
else
{
toPlay!!.setDataSource(mot!!.getSonido())
}

}
catch (e:IOException) {
e.printStackTrace()
}

try
{
toPlay!!.prepare()
}
catch (e:IOException) {
e.printStackTrace()
}

toPlay!!.start()

Toast.makeText(this@CloserLook, R.string.reproduciendo_sonido, Toast.LENGTH_SHORT).show()

toPlay!!.setOnCompletionListener(object:MediaPlayer.OnCompletionListener {
public override fun onCompletion(mp:MediaPlayer) {
botonStop.hide()
botonGrabar.setEnabled(true)
botonPlay.setEnabled(true)
}
})
}
else
{

}
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

fun quitarSonido() {

val sd = File(mot!!.getSonido())
sd.delete()

mot!!.setSonido("")
DataManager.updateParoles(id, mot!!)

botonPlay.hide()
botonStop.hide()
botonQuitarSonido.hide()

Toast.makeText(this@CloserLook, R.string.sonidoEliminado, Toast.LENGTH_SHORT).show()
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public override fun onCreateOptionsMenu(menu:Menu):Boolean {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.menu_closelook, menu)
return true
}

public override fun onOptionsItemSelected(item:MenuItem):Boolean {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
val idm = item.getItemId()


if (idm == R.id.edit_button)
{
clickEdit()
return true
}

if (idm == R.id.delete_button)
{
DataManager.delete(id)
finish()
return true
}

if (idm == R.id.share_button)
{
shareTheWord()
return true
}


return super.onOptionsItemSelected(item)
}

public override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults:IntArray) {
when (requestCode) {
MY_PERMISSIONS_REQUEST -> {
if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
{
Log.d("Home", "Permission Granted")
recordingAudio()
}
else
{
Toast.makeText(this, getResources().getString(R.string.no_record_allowed), Toast.LENGTH_SHORT).show()
}
}
}// Add additional cases for other permissions you may have asked for
}

private fun recordingAudio() {
isRecording = true
botonStop.setEnabled(true)
botonPlay.setEnabled(false)
botonGrabar.setEnabled(false)
botonQuitarSonido.setEnabled(false)

botonStop.show()

record = MediaRecorder()
record!!.setAudioSource(MediaRecorder.AudioSource.MIC)
record!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
record!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
val recordedWord = String(soundPath + "sound_" + mot!!.getMotEn2() + ".3gp")
record!!.setOutputFile(recordedWord)

try
{
record!!.prepare()
}
catch (e:IOException) {
e.printStackTrace()
}

record!!.start()
Toast.makeText(this@CloserLook, R.string.grabando_audio, Toast.LENGTH_SHORT).show()

mot!!.setSonido(recordedWord)
DataManager.updateParoles(id, mot!!)
}
}

/** Completar el acceso a la actividad, si mando el id o lo que sea desde MainActivity.
 *
 *
*/

*/