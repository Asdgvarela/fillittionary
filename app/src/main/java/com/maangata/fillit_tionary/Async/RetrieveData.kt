package com.maangata.fillit_tionary.Async

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Model.MotsList
import com.maangata.fillit_tionary.R
import com.maangata.fillit_tionary.Utils.Constants.LANGUES
import java.lang.ref.WeakReference

class RetrieveData(private var dataRequested: Int = -1, var context: WeakReference<Context>): AsyncTask<Void, Void, MotsList>() {

    override fun doInBackground(vararg p0: Void?): MotsList {
        val motList = MotsList()

        if (dataRequested == LANGUES) {
            val cursor = DataManager.wantLangue(context.get()!!)

            while (cursor.moveToNext()) {
                if (!cursor.isNull(1)) {
                    if (cursor.getString(1) == "") {
                        Toast.makeText(context.get(), R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                        DataManager.delete(cursor.getLong(0), context.get()!!)

                    } else if (cursor.getString(1) == "-") {
                        if (!motList.langues.contains<Any>(context.get()!!.getString(R.string.sinIdioma).toUpperCase())) {
                            motList.langues.add(cursor.getString(R.string.sinIdioma).toUpperCase())
                        }

                    } else {
                        if (!motList.langues.contains<Any>(cursor.getString(1).toUpperCase())) {
                            motList.langues.add(cursor.getString(1).toUpperCase())
                        }
                    }
                }
            }
        }

        return motList
    }

    override fun onPostExecute(result: MotsList?) {
        super.onPostExecute(result)
    }
}