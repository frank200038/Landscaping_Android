package com.example.landscaping.ui.dashboard

import android.os.Bundle
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
import androidx.lifecycle.ViewModelProviders
import com.example.landscaping.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.coroutines.InternalCoroutinesApi

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(relativeLayout, savedInstanceState)
        val values : ArrayList<String> = arrayListOf("Happy","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD","SAD")
        val adapter = EstimationArrayAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1,values)
        val listView : ListView = view.findViewById(android.R.id.list)
        listView.adapter = adapter

    }
}
