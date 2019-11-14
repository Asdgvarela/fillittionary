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
class AdaptadorMain(val mContext: Context) : BaseAdapter() {

    private lateinit var mMot: Mot
    var mList: List<Mot> = ArrayList()
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
        return mList[p0].id!!
    }

    override fun getCount(): Int {
        return mList.size
    }

    fun setList(mList: List<Mot>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    private inner class ViewHolder(view: View) {
        var motEn1: TextView = view.findViewById(R.id.firstcol)
        var motEn2: TextView = view.findViewById(R.id.secondcol)
        var func: TextView = view.findViewById(R.id.func)
    }
}

