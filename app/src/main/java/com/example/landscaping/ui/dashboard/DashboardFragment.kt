package com.example.landscaping.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.landscaping.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.coroutines.InternalCoroutinesApi

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var values:ArrayList<Estimation> = ArrayList(1)

    @InternalCoroutinesApi
    private lateinit var estimationViewModel : EstimationViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }




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
}
