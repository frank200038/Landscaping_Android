package com.jfcgraphicsllc.landscaping

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.jfcgraphicsllc.landscaping.databinding.InfoDisplayBinding
import com.jfcgraphicsllc.landscaping.ui.home.HomeFragment
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.collections.ArrayList


class DisplayHistoryActivity : AppCompatActivity() {

    private lateinit var binding: InfoDisplayBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var serviceArray: ArrayList<EditText> = arrayListOf()
    private var ftArray1: ArrayList<EditText> = arrayListOf()
    private var ftArray2: ArrayList<EditText> = arrayListOf()
    private var sqftArray: ArrayList<EditText> = arrayListOf()
    private var costArray: ArrayList<EditText> = arrayListOf()
    private var userDataAndTotalArray: ArrayList<EditText> = arrayListOf()
    private lateinit var estimation: Estimation

    @InternalCoroutinesApi
    private lateinit var estimationViewModel: EstimationViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        binding = InfoDisplayBinding.inflate(layoutInflater)
        estimationViewModel = ViewModelProvider(this).get(EstimationViewModel::class.java)
        val view = binding.root
        setContentView(view)
        val actionBar = this.supportActionBar
        actionBar?.hide()
        Log.d("ActionBar", "${actionBar}")
        val intent = intent
        estimation = intent.getSerializableExtra("historyData") as Estimation
        Log.d("Data", "${estimation}")
        addAllArrays()
        processRetrievedData(
            estimation,
            serviceArray,
            ftArray1,
            ftArray2,
            sqftArray,
            costArray,
            userDataAndTotalArray
        )

        binding.edit.setOnClickListener {
            val fragment = HomeFragment()
            val fragmentManager = this.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putSerializable("data", estimation)
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.replaceFragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            binding.marginHistory.visibility = View.INVISIBLE
        }
        binding.deleteHistory.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Remove Entry")
            alertDialog.setMessage("Do you really want to remove this entry?")
            alertDialog.setPositiveButton("Yes") { dialog, which ->
                estimationViewModel.remove(estimation)
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Entry Removed")
                alertDialog.setMessage("The Entry has been removed with scuess")
                alertDialog.setPositiveButton("Exit") { dialog, which ->
                    finish()
                }
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            alertDialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val alert = alertDialog.create()
            alert.show()
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

        }
    }

    fun addAllArrays() {
        val serviceToAdd = listOf(
            binding.service1History,
            binding.service2History,
            binding.service3History,
            binding.service4History,
            binding.service5History
        )
        serviceArray.addAll(serviceToAdd)
        val ftToAdd1 = listOf(
            binding.ft1History,
            binding.ft12History,
            binding.ft13History,
            binding.ft14History,
            binding.ft15History
        )
        ftArray1.addAll(ftToAdd1)
        val ftToAdd2 = listOf(
            binding.ft2History,
            binding.ft22History,
            binding.ft23History,
            binding.ft24History,
            binding.ft25History
        )
        ftArray2.addAll(ftToAdd2)
        val sqftToAdd = listOf(
            binding.sqft1History,
            binding.sqft2History,
            binding.sqft3History,
            binding.sqft4History,
            binding.sqft5History
        )
        sqftArray.addAll(sqftToAdd)
        val costToAdd = listOf(
            binding.cost1History,
            binding.cost2History,
            binding.cost3History,
            binding.cost4History,
            binding.cost5History
        )
        costArray.addAll(costToAdd)
        val userDataAndTotalToAdd = listOf(
            binding.nameHistory,
            binding.phoneHistory,
            binding.totalsqftHistory,
            binding.totalcostHistory
        )
        userDataAndTotalArray.addAll(userDataAndTotalToAdd)
    }

    fun processRetrievedData(estimation: Estimation, vararg array: ArrayList<EditText>) {
        for (index in 0..array.count() - 1) {
            when (index) {
                0 -> for (i in 0..array[index].count() - 1) {
                    when (estimation.service[i]) {
                        "0" -> array[index][i].setText("Choose A Services")
                        "1" -> array[index][i].setText("	Pavers patios walkway jobs 	")
                        "2" -> array[index][i].setText("	Concrete patios walkway jobs 	")
                        "3" -> array[index][i].setText("	Black top driveway	")
                        "4" -> array[index][i].setText("	Solid white pvc fence	")
                        "5" -> array[index][i].setText("	4 Foot solid pcv gate 	")
                        "6" -> array[index][i].setText("	Designed top pvc 6x8 pvc w fence	")
                        "7" -> array[index][i].setText("	Designed 4 foot gate	")
                        "8" -> array[index][i].setText("	Stockade wood fence 	")
                        "9" -> array[index][i].setText("	4 Foot stockade wood gate 	")
                        "10" -> array[index][i].setText("	Aluminum decorative fence 	")
                        "11" -> array[index][i].setText("	Aluminum decorative 4 foot gate 	")
                        "12" -> array[index][i].setText("	Black, brown and red  mulch 	")
                        "13" -> array[index][i].setText("	Natural brown mulch 	")
                        "14" -> array[index][i].setText("	Spring clean ups 	")
                        "15" -> array[index][i].setText("	Fall clean ups 	")
                        "16" -> array[index][i].setText("	Trimming 	")
                    }
                }
                1 -> for (i in 0..array[index].count() - 1) {
                    array[index][i].setText(estimation.ft1[i])
                }
                2 -> for (i in 0..array[index].count() - 1) {
                    array[index][i].setText(estimation.ft2[i])
                }
                3 -> for (i in 0..array[index].count() - 1) {
                    array[index][i].setText(estimation.sqft[i])
                }
                4 -> for (i in 0..array[index].count() - 1) {
                    array[index][i].setText(estimation.cost[i])
                }
                5 -> for (i in 0..array[index].count() - 1) {
                    array[index][i].setText(estimation.userDataAndTotal[i])
                }
            }
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if(fm.backStackEntryCount > 0)
        {
            finish()
        }
        super.onBackPressed()
    }

}

