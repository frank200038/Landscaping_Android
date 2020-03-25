package com.example.landscaping.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.example.landscaping.R
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var service1 : Spinner
    private lateinit var service2 : Spinner
    private lateinit var service3 : Spinner
    private lateinit var ft_1_0: EditText
    private lateinit var ft_1_1: EditText
    private lateinit var ft_1_2: EditText
    private lateinit var ft_1_3: EditText
    private lateinit var ft_1_4: EditText
    private lateinit var ft_2_0: EditText
    private lateinit var ft_2_1: EditText
    private lateinit var ft_2_2: EditText
    private lateinit var ft_2_3: EditText
    private lateinit var ft_2_4: EditText
    private lateinit var sqft_0: EditText
    private lateinit var sqft_1: EditText
    private lateinit var sqft_2: EditText
    private lateinit var sqft_3: EditText
    private lateinit var sqft_4: EditText
    private lateinit var cost_0: EditText
    private lateinit var cost_1: EditText
    private lateinit var cost_2: EditText
    private lateinit var cost_3: EditText
    private lateinit var cost_4: EditText
    private lateinit var name : EditText
    private lateinit var phone : EditText
    private lateinit var costTotal : EditText
    private lateinit var sqftTotal : EditText
    private var serviceArray:ArrayList<Spinner> = arrayListOf()
    private var ftArray1 : ArrayList<EditText> = arrayListOf()
    private var ftArray2 : ArrayList<EditText> = arrayListOf()
    private var sqftArray : ArrayList<EditText> = arrayListOf()
    private var costArray : ArrayList<EditText> = arrayListOf()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.name
        phone = view.phone
        service1 = view.service1
        ft_1_0 = view.ft1 as EditText
        ft_1_1 = view.ft12 as EditText
        ft_1_2 = view.ft13 as EditText
        ft_1_3 = view.ft14 as EditText
        ft_1_4 = view.ft15 as EditText
        ft_2_0 = view.ft2 as EditText
        ft_2_1 = view.ft22 as EditText
        ft_2_2 = view.ft23 as EditText
        ft_2_3 = view.ft24 as EditText
        ft_2_4 = view.ft25 as EditText
        sqft_0 = view.sqft1 as EditText
        sqft_1 = view.sqft2 as EditText
        sqft_2 = view.sqft3 as EditText
        sqft_3 = view.sqft4 as EditText
        sqft_4 = view.sqft5 as EditText
        cost_0 = view.cost1 as EditText
        cost_1 = view.cost2 as EditText
        cost_2 = view.cost3 as EditText
        cost_3 = view.cost4 as EditText
        cost_4 = view.cost5 as EditText
        costTotal = view.totalcost as EditText
        sqftTotal = view.totalsqft as EditText
    }
}
