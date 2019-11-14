package com.maangata.fillit_tionary.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.maangata.fillit_tionary.Activities.*
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.ID
import com.maangata.fillit_tionary.Utils.Constants.LANGUE
import com.maangata.fillit_tionary.Utils.Constants.NUEVO
import java.io.*

class Utils {

    companion object {

        fun exportData(context: Context): Boolean {
            var result = false
            val outFileName = EditText(context)
            outFileName.setText("fillit_tionaryDB.csv")
            val dataManager = DataManager(context as Application)

            AlertDialog.Builder(context)
                .setTitle(R.string.guardar)
                .setMessage(R.string.exportarMensaje)
                .setView(outFileName)
                .setPositiveButton(R.string.guardar) { dialog, whichButton ->
                    val csvHeader = context.getString(R.string.palabra).toUpperCase() +
                            "\t" + context.getString(R.string.traduccion).toUpperCase() +
                            "\t" + context.getString(R.string.tipo).toUpperCase() +
                            "\t" + context.getString(R.string.nota).toUpperCase() +
                            "\t" + context.getString(R.string.idioma).toUpperCase() +
                            "\t" + context.getString(R.string.sonido).toUpperCase() + "\n"
                    var csvValues = ""

                    // Creo el directorio si no existe.
                    val sd = File(Environment.getExternalStorageDirectory().toString() + File.separator + "Fillit_tionary")
                    sd.mkdirs()

                    try {
                        val outFile = File(sd, outFileName.text.toString())
                        val fileWriter = FileWriter(outFile)
                        val out = BufferedWriter(fileWriter)

//                        val db = DataManager.createDB(context).writableDatabase
                        val db = dataManager.getFillitDb()
                        val cursor = db.fillitDao().getLanguesMots()

                        out.write(csvHeader)
                        for (mMot in cursor.value!!) {
                            csvValues = mMot.motEn1 + "\t"
                            csvValues += mMot.motEn2 + "\t"
                            csvValues += mMot.tipo + "\t"
                            csvValues += mMot.nota!!.replace("\n", "   ///   ") + "\t"
                            if (mMot.idioma == "-" || mMot.idioma == ""
                                || mMot.idioma!!.toUpperCase() == context.getString(R.string.idioma).toUpperCase()
                            ) {
                                csvValues += context.getString(R.string.sinIdioma).toUpperCase() + "\t"
                            } else {
                                csvValues += mMot.idioma!!.toUpperCase() + "\t"
                            }
                            csvValues += mMot.sonido + "\n"
                            // 3 spaces and 3 /// and 3 spaces to translate a \n into a excel.

                            out.write(csvValues)
                        }

                        out.close()
                        val msg = context.getString(R.string.dbguardada)

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        result = true
                    } catch (error: Exception) {
                        result = false
                    }
                }
                .setNegativeButton(R.string.cancelar, null)
                .show()
                return result
        }

//        fun importData(context: Context)  {
//            val entry = EditText(context)
//            entry.setText("fillit_tionaryDB.csv")
//            val fileLocation = File(Environment.getExternalStorageDirectory().toString() + "/Fillit_tionary")
//            AlertDialog.Builder(context)
//                .setTitle(R.string.importar)
//                .setMessage(R.string.mensajeImport)
//                .setView(entry)
//                .setPositiveButton(R.string.importar) { dialog, whichButton ->
//                    val dbName = fileLocation.toString() + "/" + entry.text.toString()
//                    var file: FileReader? = null
//                    try {
//                        file = FileReader(dbName)
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    }
//
//                    if (file == null) {
//                        Toast.makeText(context, R.string.errorImport, Toast.LENGTH_SHORT).show()
//
//                    } else {
//                        val buffer = BufferedReader(file)
//                        val tableName = "fillittionary"
//                        val columns =
//                            "palabra, traduccion, funcion, nota, idioma, sonido" // Supongo que hay que añadir "sonido" porque es un campo que se representa en CloserLook. Los campos que no se representan pueden dejarse vacíos *supongo*.
//                        val str1 = "INSERT INTO $tableName ($columns) values("
//                        val str2 = ");"
//
//                        val dataManager = DataManager(context as Application)
//                        val db = dataManager.getFillitDb()
//                        db.beginTransaction()
//                        try {
//                            val line = buffer.readLine()
//                            while (line != null) {
//                                val sb = StringBuilder(str1)
//                                val str = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//                                /** The following code is used to import every DB, no matter how big
//                                 * it is or the number of rows it has.
//                                 *
//                                 */
//
//                                /** The following code is used to import every DB, no matter how big
//                                 * it is or the number of rows it has.
//                                 *
//                                 */
//                                if (str.size == 6) {
//                                    for (i in str.indices) {
//                                        if (i == str.size - 2) {
//
//                                            if (str[i] == "-") {
//                                                str[i] = context.getString(R.string.sinIdioma).toUpperCase()
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "', ")
//                                            } else if (str[i] == context.getString(R.string.idioma).toUpperCase()) {
//                                                str[i] = context.getString(R.string.sinIdioma).toUpperCase()
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "', ")
//                                            } else {
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "', ")
//                                            }
//
//                                        } else {
//                                            sb.append("'" + str[i].replace("   ///   ", "\n") + "'")
//                                            if (i != str.size - 1) {
//                                                sb.append(", ")
//                                            }
//                                        }
//                                    }
//
//                                } else if (str.size == 5) {
//                                    for (i in str.indices) {
//                                        if (i == str.size - 1) {
//                                            if (str[i] == "-") {
//                                                str[i] = context.getString(R.string.sinIdioma).toUpperCase()
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "'")
//                                            } else if (str[i] == context.getString(R.string.idioma).toUpperCase()) {
//                                                str[i] = context.getString(R.string.sinIdioma).toUpperCase()
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "'")
//                                            } else {
//                                                sb.append("'" + str[i].replace("   ///   ", "\n") + "'")
//                                            }
//                                        } else {
//                                            sb.append("'" + str[i].replace("   ///   ", "\n") + "'")
//                                            if (i != str.size - 1) {
//                                                sb.append(", ")
//                                            }
//                                        }
//                                    }
//                                    sb.append(", ''")
//                                } else if (str.size == 4) {
//                                    for (i in str.indices) {
//                                        sb.append("'" + str[i].replace("   ///   ", "\n") + "', ")
//                                    }
//                                    sb.append("'" + context.getString(R.string.sinIdioma) + "', ''")
//                                } else if (str.size == 3) {
//                                    for (i in str.indices) {
//                                        sb.append("'" + str[i] + "', ")
//                                    }
//                                    sb.append("'-', '" + context.getString(R.string.sinIdioma) + "', ''")
//                                } else if (str.size == 2) {
//                                    for (i in str.indices) {
//                                        sb.append("'" + str[i] + "', ")
//                                    }
//                                    sb.append("'-', '-', '" + context.getString(R.string.sinIdioma) + "', ''")
//                                } else if (str.size == 1) {
//                                    for (i in str.indices) {
//                                        sb.append("'" + str[i] + "', ")
//                                    }
//                                    sb.append("'-', '-', '-', '" + context.getString(R.string.sinIdioma) + "', ''")
//                                } else {
//                                    Toast.makeText(context, R.string.dbInvalida, Toast.LENGTH_SHORT).show()
//                                    continue
//                                }
//
//                                sb.append(str2)
//                                db.execSQL(sb.toString())
//
//                            }
//                            Toast.makeText(context, R.string.exitoImport, Toast.LENGTH_SHORT).show()
//                            //actualizaListaPRE()
//
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//
//                        db.setTransactionSuccessful()
//                        db.endTransaction()
//                        db.close()
//
//                    }
//                }
//                .setNegativeButton("Cancelar", null)
//                .show()
//        }

        fun emailDb(context: Context) {
            val sd = File(Environment.getExternalStorageDirectory().toString() + "/Fillit_tionary")
            sd.mkdirs()
            val outFile = File(sd, "emailed_fillit.csv")

            val csvHeader = context.getString(R.string.palabra).toUpperCase() +
                    "\t" + context.getString(R.string.traduccion).toUpperCase() +
                    "\t" + context.getString(R.string.tipo).toUpperCase() +
                    "\t" + context.getString(R.string.nota).toUpperCase() +
                    "\t" + context.getString(R.string.idioma).toUpperCase() +
                    "\t" + context.getString(R.string.sonido).toUpperCase() + "\n"
            var csvValues = ""

            try {
                val fileWriter = FileWriter(outFile)
                val out = BufferedWriter(fileWriter)

                val dataManager = DataManager(context as Application)
                val db = dataManager.getFillitDb()
                val cursor = db.fillitDao().getLanguesMots()

                out.write(csvHeader)
                for (mMot in cursor.value!!) {
                    csvValues = mMot.motEn1 + "\t"
                    csvValues += mMot.motEn2 + "\t"
                    csvValues += mMot.tipo + "\t"
                    csvValues += mMot.nota!!.replace("\n", "   ///   ") + "\t"
                    if (mMot.nota == "-" || mMot.nota == ""
                        || mMot.nota!!.toUpperCase() == context.getString(R.string.idioma).toUpperCase()
                    ) {
                        csvValues += context.getString(R.string.sinIdioma).toUpperCase() + "\t"
                    } else {
                        csvValues += mMot.nota!!.toUpperCase() + "\t"
                    }
                    csvValues += mMot.sonido + "\n"
                    // 3 spaces and 3 /// and 3 spaces to translate a \n into a excel.
                    out.write(csvValues)
                }

                out.close()

            } catch (e: IOException) {
            }


            val emailIntent = Intent(Intent.ACTION_SEND)
            // Define the type of extra info in the intent
            emailIntent.type = "vnd.android.cursor.dir/email"
            // Getting the attachment
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile))
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.enviar_con)))
        }

        fun addWord(langue: String, context: Context, app: Application) {
            val dataManager = DataManager(app)
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(NUEVO, true)
            intent.putExtra(LANGUE, langue)
            (context as MainActivity).startActivityForResult(intent, Constants.CODE_ADD)
        }

        fun startQuiz(langue: String, numOfWords: Int, context: Context) {
            val maxNumWords = 10

            if (numOfWords > maxNumWords) {
                val edit10 = EditText(context)
                edit10.setHint(R.string.buscarHint)
                AlertDialog.Builder(context)
                    .setTitle(R.string.numeroPreguntas)
                    .setView(edit10)
                    .setPositiveButton(R.string.listo) { dialog, whichButton ->
                        val noPreg = edit10.text.toString()

                        if (!noPreg.matches("[0-9]+".toRegex())) {
                            Toast.makeText(context, R.string.soloNumeros, Toast.LENGTH_SHORT).show()

                        } else if (Integer.parseInt(noPreg) >= numOfWords) {
                            val str = context.getString(R.string.demasiadasPreg) + " " + numOfWords
                            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

                        } else {
                            val defNoPreg = Integer.parseInt(noPreg)
                            val str = langue
                            val i = Intent(context, QuizMeActivity::class.java)
                            i.putExtra(Intent.EXTRA_TEXT, str)
                            i.putExtra("noPregs", defNoPreg)
                            context.startActivity(i)
                            (context as MainActivity).finish()
                        }
                    }
                    .setNegativeButton(R.string.todaviaNo, null)
                    .show()


            } else {
                AlertDialog.Builder(context)
                    .setTitle(R.string.empezarQuiz)
                    .setPositiveButton(R.string.listo) { _, _ ->
                        val str = langue
                        val i = Intent(context, QuizMeActivity::class.java)
                        i.putExtra(Intent.EXTRA_TEXT, str)
                        context.startActivity(i)
                        (context as MainActivity).finish()
                    }

                    .setNegativeButton(R.string.todaviaNo, null)
                    .show()

            }
        }

        fun searchWord(context: Activity, app: Application) {
            val entrada = EditText(context)
            entrada.setHint(R.string.buscarHint)
            AlertDialog.Builder(context)
                .setTitle(R.string.buscarPal)
                .setView(entrada)
                .setPositiveButton("Ok") { _, _ ->
                    val string = entrada.text.toString()
                    val dataManager = DataManager(app)
                    val idmot = dataManager.searchPalabra(string)
                    val idmot2 = dataManager.searchTraduccion(string)

                    if ((idmot == -1L && idmot2 == -1L) || (idmot == 0L && idmot2 == 0L)) {
                        Toast.makeText(context, R.string.toastMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        if (idmot != -1L && idmot != 0L) {
                            val intent = Intent(context, CloserLook::class.java)
                            intent.putExtra("id", idmot)
                            (context).startActivityForResult(intent, Constants.CODE_SEARCH)
                        } else {
                            val intent = Intent(context, CloserLook::class.java)
                            intent.putExtra("id", idmot2)
                            (context).startActivityForResult(intent, Constants.CODE_SEARCH)
                        }
                    }
                }

                .setNegativeButton(R.string.cancelar, null)
                .show()
        }

        fun addWordForLanguage(context: Activity, app:Application) {
            val entradaLang = EditText(context)
            entradaLang.setHint(R.string.buscarHint)
            AlertDialog.Builder(context)
                .setTitle(R.string.enterLang)
                .setView(entradaLang)
                .setPositiveButton(Constants.OK) { _, _ ->
                    if (entradaLang.text.toString().equals("")) {
                        Toast.makeText(context, R.string.idiomaVacio, Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(app, EditActivityForLangue::class.java)
                        intent.putExtra(NUEVO, true)
                        intent.putExtra(LANGUE, entradaLang.text.toString())

                        context.startActivityForResult(intent, Constants.TONEWLANGUE)

                        val newLang = context.getString(R.string.nuevoIdioma) + " " + entradaLang.text.toString()
                        Toast.makeText(context, newLang, Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancelar, null)
                .show()

        }
    }
}