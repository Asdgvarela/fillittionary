package com.maangata.fillit_tionary.Mvp

import android.content.Context
import android.widget.Toast
import com.maangata.fillit_tionary.Data.DataManager
import com.maangata.fillit_tionary.Interfaces.MainContract
import com.maangata.fillit_tionary.R

class LanguesModelCallbackImplem(val context: Context): MainContract.Langues.ModelCallback {

    override fun getLangues(listener: MainContract.Langues.ModelCallback.OnFinishedLoadingLanguesListener) {
        val motList = ArrayList<String>()

        val cursor = DataManager.wantLangue(context)

        while (cursor.moveToNext()) {
            if (!cursor.isNull(1)) {

                if (cursor.getString(1) == "") {
                    Toast.makeText(context, R.string.avisoDeCorrupcion, Toast.LENGTH_SHORT).show()
                    DataManager.delete(cursor.getLong(0), context)

                } else if (cursor.getString(1) == "-") {
                    if (!motList.contains<Any>(context.getString(R.string.sinIdioma).toUpperCase())) {
                        motList.add(cursor.getString(R.string.sinIdioma).toUpperCase())
                    }

                } else {
                    if (!motList.contains<Any>(cursor.getString(1).toUpperCase())) {
                        motList.add(cursor.getString(1).toUpperCase())
                    }
                }
            }
        }

        listener.onFinished(motList)
        cursor.close()
    }
}