package com.maangata.fillit_tionary.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.R

/**
 * Created by zosdam on 1/09/15.
 */
class AdaptadorMain(val mContext: Context, var mList: ArrayList<Mot>) : BaseAdapter() {

    private lateinit var mMot: Mot

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var row: View? = view
        mMot = mList[position]

        val viewHolder: ViewHolder

        if (row == null) {
            val inflater = LayoutInflater.from(mContext)
            row = inflater.inflate(R.layout.elemento_lista, viewGroup, false)
            viewHolder = ViewHolder(row!!)

            row.tag = viewHolder
        } else {
            viewHolder = row.tag as ViewHolder
        }

        viewHolder.motEn1.text = mMot.motEn1
        viewHolder.motEn2.text = mMot.motEn2
        viewHolder.func.text = mMot.tipo

        return row
    }

    override fun getItem(p0: Int): Any {
        return mList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return mList[p0].id
    }

    override fun getCount(): Int {
        return mList.size
    }
//
//    override fun bindView(vista: View, contexto: Context, c: Cursor) {
//        funcion = vista.findViewById<View>(R.id.func) as TextView
//        val temp = c.getString(c.getColumnIndex("funcion")).substring(
//            0,
//            1
//        ) + c.getString(c.getColumnIndex("funcion")).substring(1)
//        funcion.text = temp //** Also used here getColumnIndex insted of this one*/
//
//        motEn1 = vista.findViewById<View>(R.id.firstcol) as TextView
//        val temp1 = c.getString(c.getColumnIndex("palabra")).substring(
//            0,
//            1
//        ) + c.getString(c.getColumnIndex("palabra")).substring(1)
//        motEn1.text = temp1
//
//        motEn2 = vista.findViewById<View>(R.id.secondcol) as TextView
//        val temp2 = c.getString(c.getColumnIndex("traduccion")).substring(
//            0,
//            1
//        ) + c.getString(c.getColumnIndex("traduccion")).substring(1)
//        motEn2.text = temp2
//    }

    private inner class ViewHolder(view: View) {
        var motEn1: TextView = view.findViewById(R.id.firstcol)
        var motEn2: TextView = view.findViewById(R.id.secondcol)
        var func: TextView = view.findViewById(R.id.func)
    }
}

