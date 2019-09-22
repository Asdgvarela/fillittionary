package com.maangata.fillit_tionary.Adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import com.maangata.fillit_tionary.R

/**
 * Created by zosdam on 1/09/15.
 */
class AdaptadorCursor
/**
 * Simplemente inicia el adaptador.
 * @param contexto
 * @param c
 */
    (
    contexto: Context, c: Cursor) : CursorAdapter(contexto, c, false) {

    private lateinit var motEn1: TextView
    private lateinit var motEn2: TextView
    private lateinit var funcion: TextView
    private lateinit var inflater: LayoutInflater
    
    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.elemento_lista, parent, false)
    }

    override fun bindView(vista: View, contexto: Context, c: Cursor) {
        funcion = vista.findViewById<View>(R.id.func) as TextView
        val temp = c.getString(c.getColumnIndex("funcion")).substring(
            0,
            1
        ) + c.getString(c.getColumnIndex("funcion")).substring(1)
        funcion.text = temp //** Also used here getColumnIndex insted of this one*/

        motEn1 = vista.findViewById<View>(R.id.firstcol) as TextView
        val temp1 = c.getString(c.getColumnIndex("palabra")).substring(
            0,
            1
        ) + c.getString(c.getColumnIndex("palabra")).substring(1)
        motEn1.text = temp1

        motEn2 = vista.findViewById<View>(R.id.secondcol) as TextView
        val temp2 = c.getString(c.getColumnIndex("traduccion")).substring(
            0,
            1
        ) + c.getString(c.getColumnIndex("traduccion")).substring(1)
        motEn2.text = temp2
    }
}

