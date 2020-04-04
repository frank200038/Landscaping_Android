package com.jfcgraphicsllc.landscaping

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.jfcgraphicsllc.landscaping.databinding.InfoDisplayBinding
//import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class DisplayHistoryActivity : AppCompatActivity() {

    private lateinit var binding : InfoDisplayBinding
    private var serviceArray:ArrayList<EditText> = arrayListOf()
    private var ftArray1 : ArrayList<EditText> = arrayListOf()
    private var ftArray2 : ArrayList<EditText> = arrayListOf()
    private var sqftArray : ArrayList<EditText> = arrayListOf()
    private var costArray : ArrayList<EditText> = arrayListOf()
    private var userDataAndTotalArray : ArrayList<EditText> = arrayListOf()
    //private lateinit var analytics:FirebaseAnalytics

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InfoDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = this.supportActionBar
        actionBar?.hide()
        Log.d("ActionBar","${actionBar}")
        val intent = getIntent()
        val estimation = intent.getSerializableExtra("historyData") as Estimation
        Log.d("Data","${estimation}")
        addAllArrays()
        processRetrievedData(estimation,serviceArray,ftArray1,ftArray2,sqftArray,costArray,userDataAndTotalArray)

//         analytics = FirebaseAnalytics.getInstance(this)
//         analytics.setCurrentScreen(this,"DisplayHistory",null)
    }

    fun addAllArrays()
    {
        val serviceToAdd = listOf(binding.service1History,binding.service2History,binding.service3History,binding.service4History,binding.service5History)
        serviceArray.addAll(serviceToAdd)
        val ftToAdd1 = listOf(binding.ft1History,binding.ft12History,binding.ft13History,binding.ft14History,binding.ft15History)
        ftArray1.addAll(ftToAdd1)
        val ftToAdd2 = listOf(binding.ft2History,binding.ft22History,binding.ft23History,binding.ft24History,binding.ft25History)
        ftArray2.addAll(ftToAdd2)
        val sqftToAdd = listOf(binding.sqft1History,binding.sqft2History,binding.sqft3History,binding.sqft4History,binding.sqft5History)
        sqftArray.addAll(sqftToAdd)
        val costToAdd = listOf(binding.cost1History,binding.cost2History,binding.cost3History,binding.cost4History,binding.cost5History)
        costArray.addAll(costToAdd)
        val userDataAndTotalToAdd = listOf(binding.nameHistory,binding.phoneHistory,binding.totalsqftHistory,binding.totalcostHistory)
        userDataAndTotalArray.addAll(userDataAndTotalToAdd)
    }

    fun processRetrievedData(estimation: Estimation, vararg array: ArrayList<EditText>)
    {
        for(index in 0..array.count()-1) {
            when (index) {
                0 -> for (i in 0..array[index].count() - 1) {
                    when (estimation.service[i]) {
                        "0" -> array[index][i].setText("Choose A Services")
                        "1" -> array[index][i].setText("Trimming")
                        "2" -> array[index][i].setText("Planting")
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
}
