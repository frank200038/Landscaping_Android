package com.example.landscaping

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.row_view.view.*
import java.util.*
import kotlin.collections.ArrayList


class EstimationArrayAdapter(context: Context, @LayoutRes private var layoutRes:Int, private var value:ArrayList<Estimation>):ArrayAdapter<Estimation>(context,layoutRes,value),Filterable
{
    private var filtered : ArrayList<Estimation> = value
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
        val estimation = filtered[position]
        viewHolder.name.setText(estimation.name)
        viewHolder.phone.setText(estimation.phone)
        return rowView
    }

    fun setValue(value:ArrayList<Estimation>)
    {
        this.value = value
        this.filtered = value
        notifyDataSetChanged()
    }

    fun getValueAtIndex(index:Int): Estimation
    {
        return this.filtered[index]
    }

    override fun  getCount():Int
    {
    return filtered.count()
    }

    override fun getFilter(): Filter {
        val filter = object : Filter()
        {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if(constraint == null || constraint.count() == 0)
                {
                    filterResults.count = value.count()
                    filterResults.values = value
                }
                else
                {
                    val results : ArrayList<Estimation> = arrayListOf()
                    val search = constraint.toString().toLowerCase(Locale.ROOT)
                    for (item in value)
                    {
                        if (item.name.contains(search) || item.phone.contains(search))
                        {
                            results.add(item)
                            Log.d("Filter","${item}")
                        }
                    }
                    filterResults.count = results.count()
                    filterResults.values = results
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filtered = results?.values as ArrayList<Estimation>
                notifyDataSetChanged()
            }
        }

        return filter

    }

}