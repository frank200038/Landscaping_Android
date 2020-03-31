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
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class DisplayHistoryActivity : AppCompatActivity() {
    private lateinit var service1 : EditText
    private lateinit var service2 : EditText
    private lateinit var service3 : EditText
    private lateinit var service4 : EditText
    private lateinit var service5 : EditText
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
    private lateinit var screenShot : Button
    private var serviceArray:ArrayList<EditText> = arrayListOf()
    private var ftArray1 : ArrayList<EditText> = arrayListOf()
    private var ftArray2 : ArrayList<EditText> = arrayListOf()
    private var sqftArray : ArrayList<EditText> = arrayListOf()
    private var costArray : ArrayList<EditText> = arrayListOf()


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val actionBar = this.supportActionBar
         actionBar?.hide()
         Log.d("ActionBar","${actionBar}")
         val intent = getIntent()
         val estimation = intent.getSerializableExtra("historyData") as Estimation
         Log.d("Data","${estimation}")
        setContentView(R.layout.info_display)
        name = findViewById(R.id.name_history)
        phone = findViewById(R.id.phone_history)
        service1 = findViewById(R.id.service1_history)
        service2 = findViewById(R.id.service2_history)
        service3 = findViewById(R.id.service3_history)
        service4 = findViewById(R.id.service4_history)
        service5 = findViewById(R.id.service5_history)
        ft_1_0 = findViewById(R.id.ft1_history)
        ft_1_1 = findViewById(R.id.ft12_history)
        ft_1_2 = findViewById(R.id.ft13_history)
        ft_1_3 = findViewById(R.id.ft14_history)
        ft_1_4 = findViewById(R.id.ft15_history)
        ft_2_0 = findViewById(R.id.ft2_history)
        ft_2_1 = findViewById(R.id.ft22_history)
        ft_2_2 = findViewById(R.id.ft23_history)
        ft_2_3 = findViewById(R.id.ft24_history)
        ft_2_4 = findViewById(R.id.ft25_history)
        sqft_0 = findViewById(R.id.sqft1_history)
        sqft_1 = findViewById(R.id.sqft2_history)
        sqft_2 = findViewById(R.id.sqft3_history)
        sqft_3 = findViewById(R.id.sqft4_history)
        sqft_4 = findViewById(R.id.sqft5_history)
        cost_0 = findViewById(R.id.cost1_history)
        cost_1 = findViewById(R.id.cost2_history)
        cost_2 = findViewById(R.id.cost3_history)
        cost_3 = findViewById(R.id.cost4_history)
        cost_4 = findViewById(R.id.cost5_history)
        costTotal = findViewById(R.id.totalcost_history)
        sqftTotal = findViewById(R.id.totalsqft_history)

        addAllArrays()
        processRetrievedData(serviceArray, "service",estimation)
        processRetrievedData(ftArray1 ,"ft1",estimation)
        processRetrievedData(ftArray2 ,"ft2",estimation)
        processRetrievedData(sqftArray,"sqft",estimation)
         processRetrievedData(costArray,"cost",estimation)
        if(estimation != null)
        {
            name.setText(estimation.name)
            phone.setText(estimation.phone)
            sqftTotal.setText(estimation.sqftTotal)
            costTotal.setText(estimation.costTotal)
        }
    }

    fun addAllArrays()
    {
        val serviceToAdd = listOf(service1,service2,service3,service4,service5)
        serviceArray.addAll(serviceToAdd)
        val ftToAdd1 = listOf(ft_1_0,ft_1_1,ft_1_2,ft_1_3,ft_1_4)
        ftArray1.addAll(ftToAdd1)
        val ftToAdd2 = listOf(ft_2_0,ft_2_1,ft_2_2,ft_2_3,ft_2_4)
        ftArray2.addAll(ftToAdd2)
        val sqftToAdd = listOf(sqft_0,sqft_1,sqft_2,sqft_3,sqft_4)
        sqftArray.addAll(sqftToAdd)
        val costToAdd = listOf(cost_0,cost_1,cost_2,cost_3,cost_4)
        costArray.addAll(costToAdd)
    }

    fun processRetrievedData(array: ArrayList<EditText>, name: String, estimation:Estimation)
    {
        if(estimation != null )
        {
            if(name == "service")
            {
                for (i in 0..array.count()-1)
                {
                    when(estimation.service[i])
                    {
                        "0" -> array[i].setText("Choose A Services")
                        "1" -> array[i].setText("Trimming")
                        "2" -> array[i].setText("Planting")
                    }
                }
            }
            else
            {
                when(name)
                {
                    "ft1" -> for(i in 0..array.count()-1){array[i].setText(estimation.ft1[i])}
                    "ft2" -> for(i in 0..array.count()-1){array[i].setText(estimation.ft2[i])}
                    "sqft" -> for(i in 0..array.count()-1){array[i].setText(estimation.sqft[i])}
                    "cost" -> for(i in 0..array.count()-1){array[i].setText(estimation.cost[i])}
                }
            }
        }
    }

    fun takeScreenShot(view: View):Bitmap
    {
        val returnedBitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)
        {
            bgDrawable.draw(canvas)
        }
        view.draw(canvas)
        return returnedBitmap
    }
}