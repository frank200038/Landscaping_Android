package com.example.landscaping

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.row_view.view.*



class EstimationArrayAdapter(context: Context, @LayoutRes private var layoutRes:Int, private var value:ArrayList<Estimation>):ArrayAdapter<Estimation>(context,layoutRes,value)
{
    class ViewHolder
    {
         lateinit var name:TextView
         lateinit var phone: TextView
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        if(rowView == null)
        {
            val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.row_view,parent,false)
            val viewHolder = ViewHolder()
            viewHolder.name = rowView.name_list
            viewHolder.phone = rowView.phone_list
            rowView.tag = viewHolder
        }
        val viewHolder = rowView!!.tag as ViewHolder
        val estimation = value[position]
        viewHolder.name.setText(estimation.name)
        viewHolder.phone.setText(estimation.phone)
        return rowView
    }

    fun setValue(value:ArrayList<Estimation>)
    {
        this.value = value
        notifyDataSetChanged()
    }

    fun getValueAtIndex(index:Int): Estimation
    {
        return this.value[index]
    }

    override fun  getCount():Int
    {
    return value.count()
    }

}