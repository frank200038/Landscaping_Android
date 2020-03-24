package com.example.myapplication.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.myapplication.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.quantity
import kotlin.math.round

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var quantity: EditText
    private lateinit var quantity_1: EditText
    private lateinit var quantity_2: EditText
    private lateinit var quantity_3: EditText
    private lateinit var quantity_4: EditText
    private lateinit var inch_1_0: EditText
    private lateinit var inch_1_1: EditText
    private lateinit var inch_1_2: EditText
    private lateinit var inch_1_3: EditText
    private lateinit var inch_1_4: EditText
    private lateinit var inch_2_0: EditText
    private lateinit var inch_2_1: EditText
    private lateinit var inch_2_2: EditText
    private lateinit var inch_2_3: EditText
    private lateinit var inch_2_4: EditText
    private lateinit var inchsq_0: EditText
    private lateinit var inchsq_1: EditText
    private lateinit var inchsq_2: EditText
    private lateinit var inchsq_3: EditText
    private lateinit var inchsq_4: EditText
    private lateinit var costprt_0: EditText
    private lateinit var costprt_1: EditText
    private lateinit var costprt_2: EditText
    private lateinit var costprt_3: EditText
    private lateinit var costprt_4: EditText
    private lateinit var cost_0: EditText
    private lateinit var cost_1: EditText
    private lateinit var cost_2: EditText
    private lateinit var cost_3: EditText
    private lateinit var cost_4: EditText
    private var provideText = arrayOf<Array<EditText>>()
    private var textToSave: ArrayList<String> = arrayListOf()
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this as ViewModelStoreOwner).get(HomeViewModel::class.java)
        textToSave.clear()
        Log.d("Create", "Created ${homeViewModel.data.count()}")
        Log.d("Create", "ProvideText ${provideText.count()}")
        var flag = true
        var i = prefs.getInt("i",0)
        var index = prefs.getInt("index",0)
        if(i !=0 && index != 0)
        {
            for (i in 0..i) {
                for (index in 0..index) {
                    var temp = prefs.getString("${i}_${index}", null).toString()
                    if (temp == "null") {
                        flag = false;
                        break;
                    } else {
                        textToSave.add(temp)
                    }
                }
                if (flag == false) {
                    break;
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = context.getSharedPreferences("Application", Context.MODE_PRIVATE)
        Log.d("Attach","fffff")
    }

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
        //var quantity = view.quantity as EditText
        quantity = view.quantity
        quantity_1 = view.quantity3 as EditText
        quantity_2 = view.quantity4 as EditText
        quantity_3 = view.quantity5 as EditText
        quantity_4 = view.quantity6 as EditText
        inch_1_0 = view.inch1 as EditText
        inch_1_1 = view.inch4 as EditText
        inch_1_2 = view.inch6 as EditText
        inch_1_3 = view.inch8 as EditText
        inch_1_4 = view.inch10 as EditText
        inch_2_0 = view.inch2 as EditText
        inch_2_1 = view.inch5 as EditText
        inch_2_2 = view.inch7 as EditText
        inch_2_3 = view.inch9 as EditText
        inch_2_4 = view.inch11 as EditText
        inchsq_0 = view.inchsq as EditText
        inchsq_1 = view.inchsq2 as EditText
        inchsq_2 = view.inchsq3 as EditText
        inchsq_3 = view.inchsq4 as EditText
        inchsq_4 = view.inchsq5 as EditText
        costprt_0 = view.Costprt as EditText
        costprt_1 = view.costprt2 as EditText
        costprt_2 = view.costprt3 as EditText
        costprt_3 = view.costprt4 as EditText
        costprt_4 = view.costprt5 as EditText
        cost_0 = view.cost as EditText
        cost_1 = view.cost2 as EditText
        cost_2 = view.cost3 as EditText
        cost_3 = view.cost4 as EditText
        cost_4 = view.cost5 as EditText
        var quantity_array = arrayOf(quantity, quantity_1, quantity_2, quantity_3, quantity_4)
        var inch_1_array = arrayOf(inch_1_0, inch_1_1, inch_1_2, inch_1_3, inch_1_4)
        var inch_2_array = arrayOf(inch_2_0, inch_2_1, inch_2_2, inch_2_3, inch_2_4)
        var inchsq_array = arrayOf(inchsq_0, inchsq_1, inchsq_2, inchsq_3, inchsq_4)
        var costprt_array = arrayOf(costprt_0, costprt_1, costprt_2, costprt_3, costprt_4)
        var cost_array = arrayOf(cost_0, cost_1, cost_2, cost_3, cost_4)
        provideText += quantity_array
        provideText += inch_1_array
        provideText += inch_2_array
        provideText += inchsq_array
        provideText += costprt_array
        provideText += cost_array
        if (textToSave.count() != 0)
        {
            deployArray(textToSave)
        }
        Log.d("ViewCreated", "${provideText.count()}")
        var calculate = view.calc as Button
        calculate.setOnClickListener {
            Toast.makeText(activity, "ffff", Toast.LENGTH_SHORT).show()
            var quantity_val: Int? = quantity.text.toString().toIntOrNull()
            println("${quantity_val} is a string")
            check(quantity_array, inch_1_array, inch_2_array, inchsq_array, costprt_array, cost_array)
        }

    }

    override fun onPause() {
        super.onPause()
        val editor = prefs.edit()
        textToSave.clear()
        editor?.putInt("i",provideText.count())
        editor?.putInt("index",provideText[0].count())
        for (i in 0..provideText.count() - 1) {
            for (index in 0..provideText[i].count() - 1) {
                editor?.putString("${i}_${index}", provideText[i][index].text.toString())
                Log.d("saved", "${i}_${index} and ${provideText[i][index].text}")
            }
        }
        var result = editor?.commit()
        Log.d("saved", "result: ${result}")
        Log.d("saved", "retrived: ${prefs.getString("0_0", null)}")
        Log.d("Paused", "Paused")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Destroy", "Destroyed")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("Restored", "Restored")
        Log.d("OnResume", "OnResume ${homeViewModel.data.count()}")
        if (homeViewModel.data.count() != 0) {
            deployArray(homeViewModel.data)
        }
    }

    fun deployArray(a:ArrayList<String>)
    {
        var indexI = 0
        var indexJ = 0
        for (i in 0..a.count() - 1) {
            if (i == 0) {
                provideText[indexI][indexJ].setText(a[i])
                indexJ++
            } else if (i % 5 == 0) {
                indexI++
                indexJ = 0
                provideText[indexI][indexJ].setText(a[i])
                indexJ++

            } else {
                provideText[indexI][indexJ].setText(a[i])
                indexJ++
            }
        }
    }

    fun check(a: Array<EditText>, b: Array<EditText>, c: Array<EditText>, d: Array<EditText>, e: Array<EditText>, f: Array<EditText>)
    {
        for (i in 0..a.size - 1) {
            if (a[i].text.toString().toDoubleOrNull() != null && b[i].text.toString()
                    .toDoubleOrNull() != null && c[i].text.toString().toDoubleOrNull() != null
            ) {
                var quantity = a[i].text.toString().toDouble()
                var inch_1 = b[i].text.toString().toDouble()
                var inch_2 = c[i].text.toString().toDouble()
                var sqft = round((inch_1 * inch_2) / 144.0 * 100.0) / 100
                var cost = round((sqft * 10) + 0.05 * (sqft * 10) * quantity)
                var print = round(sqft * 3.5 * quantity)
                d[i].setText(sqft.toString())
                e[i].setText(cost.toString())
                f[i].setText(print.toString())

                println("they are not null")
            }
        }
    }
}


