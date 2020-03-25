package com.example.landscaping.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.example.landscaping.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.security.cert.CertPathValidatorException

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var service1 : Spinner
    private lateinit var service2 : Spinner
    private lateinit var service3 : Spinner
    private lateinit var service4 : Spinner
    private lateinit var service5 : Spinner
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
    private lateinit var clear : Button
    private var serviceArray:ArrayList<Spinner> = arrayListOf()
    private var ftArray1 : ArrayList<EditText> = arrayListOf()
    private var ftArray2 : ArrayList<EditText> = arrayListOf()
    private var sqftArray : ArrayList<EditText> = arrayListOf()
    private var costArray : ArrayList<EditText> = arrayListOf()
    private lateinit var prefs : SharedPreferences
    private lateinit var serviceArrayToSave : ArrayList<Any>
    private lateinit var ftArrayToSave1 : ArrayList<Any>
    private lateinit var ftArrayToSave2 : ArrayList<Any>
    private lateinit var sqftArrayToSave : ArrayList<Any>
    private lateinit var costArrayToSave : ArrayList<Any>
    private var userDataAndTotalToSave : ArrayList<String> = arrayListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceArrayToSave = retrievePrefs(prefs,"Service",true)
        ftArrayToSave1 = retrievePrefs(prefs,"ft1",false)
        ftArrayToSave2 = retrievePrefs(prefs, "ft2",false)
        sqftArrayToSave = retrievePrefs(prefs,"sqft",false)
        costArrayToSave = retrievePrefs(prefs,"cost",false)
        userDataAndTotalToSave.add(retrieveUserDataAndTotal(prefs,"name"))
        userDataAndTotalToSave.add(retrieveUserDataAndTotal(prefs,"phone"))
        userDataAndTotalToSave.add(retrieveUserDataAndTotal(prefs,"costTotal"))
        userDataAndTotalToSave.add(retrieveUserDataAndTotal(prefs,"sqftTotal"))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.name as EditText
        phone = view.phone as EditText
        service1 = view.service1 as Spinner
        service2 = view.service2 as Spinner
        service3 = view.service3 as Spinner
        service4 = view.service4 as Spinner
        service5 = view.service5 as Spinner
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
        clear = view.clear as Button
        addAllArrays()
        processRetrievedPrefsArray(serviceArrayToSave,serviceArray as ArrayList<Any>,true)
        processRetrievedPrefsArray(ftArrayToSave1,ftArray1 as ArrayList<Any>,false)
        processRetrievedPrefsArray(ftArrayToSave2,ftArray2 as ArrayList<Any>,false)
        processRetrievedPrefsArray(sqftArrayToSave,sqftArray as ArrayList<Any>,false)
        name.setText(userDataAndTotalToSave[0])
        phone.setText(userDataAndTotalToSave[1])
        sqftTotal.setText(userDataAndTotalToSave[2])
        costTotal.setText(userDataAndTotalToSave[3])
        //setSpinnerListener()
        view.setOnClickListener {
            it.hideKeyboard()
        }
        clear.setOnClickListener {
            clearAllData(serviceArray as ArrayList<Any>,true)
            clearAllData(ftArray1 as ArrayList<Any>,false)
            clearAllData(ftArray2 as ArrayList<Any>,false)
            clearAllData(sqftArray as ArrayList<Any>,false)
            name.setText("")
            phone.setText("")
            sqftTotal.setText("")
            costTotal.setText("")
        }
    }

    override fun onPause() {
        super.onPause()
        savePrefs(prefs,serviceArray as ArrayList<Any>,"Service",true)
        savePrefs(prefs,ftArray1 as ArrayList<Any>,"ft1",false)
        savePrefs(prefs,ftArray2 as ArrayList<Any>, "ft2",false)
        savePrefs(prefs,sqftArray as ArrayList<Any>,"sqft",false)
        savePrefs(prefs,costArray as ArrayList<Any>,"cost",false)
        val editor = prefs.edit()
        editor.putString("costTotal",costTotal.text.toString())
        editor.putString("sqftTotal",sqftTotal.text.toString())
        editor.putString("name",name.text.toString())
        editor.putString("phone",phone.text.toString())
        editor.commit()
    }
    fun View.hideKeyboard()
    {
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken,0)
    }

    fun addAllArrays()
    {
        val serviceToAdd = listOf<Spinner>(service1,service2,service3,service4,service5)
        serviceArray.addAll(serviceToAdd)
        val ftToAdd1 = listOf<EditText>(ft_1_0,ft_1_1,ft_1_2,ft_1_3,ft_1_4)
        ftArray1.addAll(ftToAdd1)
        val ftToAdd2 = listOf<EditText>(ft_2_0,ft_2_1,ft_2_2,ft_2_3,ft_2_4)
        ftArray2.addAll(ftToAdd2)
        val sqftToAdd = listOf<EditText>(sqft_0,sqft_1,sqft_2,sqft_3,sqft_4)
        sqftArray.addAll(sqftToAdd)
        val costToAdd = listOf<EditText>(cost_0,cost_1,cost_2,cost_3,cost_4)
        costArray.addAll(costToAdd)
    }


//    fun setSpinnerListener()
//    {
//        service1.onItemSelectedListener = this
//        service2.onItemSelectedListener = this
//        service3.onItemSelectedListener = this
//        service4.onItemSelectedListener = this
//        service5.onItemSelectedListener = this
//    }

    fun savePrefs (prefs: SharedPreferences, array: ArrayList<Any>,name: String, Spinner: Boolean) : Boolean
    {
        val editor = prefs.edit()
        editor.putInt("${name}_size",array.count())
        if(Spinner)
        {
            val arrayToSaveSpinner = array as ArrayList<Spinner>
            for(i in 0..array.count()-1)
            {
                editor.putString("${name}_${i}",arrayToSaveSpinner[i].selectedItem.toString())
            }
        }
        else
        {
           val arrayToSaveEditText = array as ArrayList<EditText>
            for(i in 0..array.count()-1)
            {
                editor.putString("${name}_${i}",arrayToSaveEditText[i].text.toString())
            }
        }
        val result = editor.commit()
        Log.d("Saved","${result}")
        return result
    }

    fun retrievePrefs(prefs: SharedPreferences,name: String,Spinner: Boolean) : ArrayList<Any>
    {
        val count = prefs.getInt("${name}_size",0)
        var arrayToReturn = arrayListOf<Any>()
        if(count != 0)
        {
            if(Spinner)
            {

                for(i in 0..count-1)
                {
                    val string = prefs.getString("${name}_${i}","")
                    when (string)
                    {
                        "Choose a Service" -> arrayToReturn.add(0)
                        "Trimming" -> arrayToReturn.add(1)
                        "Planting" -> arrayToReturn.add(2)
                    }

                }
            }
            else
            {
                for(i in 0..count-1)
                {
                    val string = prefs.getString("${name}_${i}","")
                    arrayToReturn.add(string.toString())
                }
            }
        }
        return arrayToReturn
    }

    fun retrieveUserDataAndTotal(prefs: SharedPreferences,need:String):String
    {
        val string = prefs.getString("${need}","")
        return string!!
    }

    fun processRetrievedPrefsArray(array:ArrayList<Any>, field:ArrayList<Any>, Spinner: Boolean)
    {
        if(array.count()!=0)
        {
            if(Spinner)
            {
                for(i in 0..array.count()-1)
                {
                    val field = field as ArrayList<Spinner>
                    val array =  array as ArrayList<Int>
                    field[i].setSelection(array[i])
                }
            }
            else if(!Spinner)
            {
                for(i in 0..array.count()-1)
                {
                    val field = field as ArrayList<EditText>
                    val array =  array as ArrayList<String>
                    field[i].setText(array[i])
                }
            }
        }
    }

    fun clearAllData(array: ArrayList<Any>, Spinner: Boolean)
    {
        if(Spinner)
        {
            val array = array as ArrayList<Spinner>
            for (i in 0..array.count()-1)
            {
                array[i].setSelection(0)
            }
        }
        else
        {
            val array = array as ArrayList<EditText>
            for (i in 0..array.count()-1)
            {
                array[i].setText("")
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
