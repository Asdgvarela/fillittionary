package com.maangata.fillit_tionary.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.maangata.fillit_tionary.R

import java.util.ArrayList

/**
 * Created by zosdam on 23/12/15.
 */
class AdaptadorPremain(var mContext: Context): BaseAdapter() {

    var langue: String = ""
    var arl: ArrayList<String> = ArrayList()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var row: View? = view
        langue = arl[position]

        val viewHolder: ViewHolder

        if (row == null) {
            val inflater = LayoutInflater.from(mContext)
            row = inflater.inflate(R.layout.premainactivity_item_layout, viewGroup, false)
            viewHolder = ViewHolder(row!!)

            row.tag = viewHolder
        } else {
            viewHolder = row.tag as ViewHolder
        }

        viewHolder.textLangue.text = langue

        return row
    }

    override fun getItem(p0: Int): Any {
       return arl[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arl.size
    }

    fun setList(mList: ArrayList<String>) {
        arl = mList
        notifyDataSetChanged()
    }

    private inner class ViewHolder(view: View) {
        var textLangue: TextView = view.findViewById(R.id.text_view_premain)
    }
}



