package com.example.landscaping.ui.dashboard

import android.app.ListActivity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.landscaping.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.row_view.view.*

import kotlinx.coroutines.InternalCoroutinesApi

class DashboardFragment : ListFragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var values:ArrayList<Estimation> = ArrayList(1)

    @InternalCoroutinesApi
    private lateinit var estimationViewModel : EstimationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //(activity as AppCompatActivity).supportActionBar!!.show()

    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(relativeLayout, savedInstanceState)

        Log.d("Count","${values.count()}")

            if(values.count() == 0)
            {
                Log.d("Empty","Empty")
            }
            val adapter = EstimationArrayAdapter(
                activity!!.applicationContext,
                android.R.layout.simple_list_item_1,
                values
            )
            val listView: ListView = view.findViewById(android.R.id.list)
            listView.adapter = adapter
            estimationViewModel = ViewModelProvider(this).get(EstimationViewModel::class.java)
            estimationViewModel.allEstimation.observe(viewLifecycleOwner, Observer { estimation ->
                estimation.map { Log.d("Observe", "${estimation[0].phone}");val adapterArray = ArrayList<Estimation>(estimation);adapter.setValue(adapterArray) }})
    }



    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        var adapter = l.adapter as EstimationArrayAdapter
        var item = adapter.getValueAtIndex(position)
        Log.d("Selected","Select ${item}")
    }


}
