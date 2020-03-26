package com.example.landscaping

import android.content.Context
import android.view.*
import android.widget.*
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.row_view.view.*



class EstimationArrayAdapter(context: Context, @LayoutRes private val layoutRes:Int, private val value:ArrayList<String>):ArrayAdapter<String>(context,layoutRes,value)
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
            rowView = inflater.inflate(R.layout.row_view,null)
            val viewHolder = ViewHolder()
            viewHolder.name = rowView.name_list
            viewHolder.phone = rowView.phone_list
            rowView.setTag(viewHolder)
        }

        val viewHolder = rowView!!.tag as ViewHolder
        val s = value[position]
        viewHolder.name.setText(s)
        viewHolder.phone.setText("fff")

//        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val rowView : View = inflater.inflate(R.layout.row_view,parent,true)
//        val nameView: TextView = rowView.name_list
//        val phoneView : TextView = rowView.phone_list
//        nameView.setText(value[position])
//        phoneView.setText("45445454545")
        return rowView

    }

}