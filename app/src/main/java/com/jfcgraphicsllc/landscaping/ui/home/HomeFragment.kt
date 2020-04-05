package com.jfcgraphicsllc.landscaping.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jfcgraphicsllc.landscaping.Estimation
import com.jfcgraphicsllc.landscaping.EstimationViewModel
import com.jfcgraphicsllc.landscaping.databinding.FragmentHomeBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


import kotlin.math.round
import kotlin.math.roundToInt

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var serviceArray:ArrayList<Spinner> = arrayListOf()
    private var ftArray1 : ArrayList<EditText> = arrayListOf()
    private var ftArray2 : ArrayList<EditText> = arrayListOf()
    private var sqftArray : ArrayList<EditText> = arrayListOf()
    private var costArray : ArrayList<EditText> = arrayListOf()
    private var userDataAndTotalArray : ArrayList<EditText> = arrayListOf()
    private lateinit var prefs : SharedPreferences
    private  var serviceArrayToSave : ArrayList<Any> = arrayListOf()
    private  var ftArrayToSave1 : ArrayList<Any> = arrayListOf()
    private  var ftArrayToSave2 : ArrayList<Any> = arrayListOf()
    private  var sqftArrayToSave : ArrayList<Any> = arrayListOf()
    private  var costArrayToSave : ArrayList<Any> = arrayListOf()
    private var userDataAndTotalToSave : ArrayList<Any> = arrayListOf()
    private val name = arrayListOf("Service","ft1","ft2","sqft","cost","userData")

    @InternalCoroutinesApi
    private lateinit var estimationViewModel : EstimationViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val root = binding.root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrievePrefs(prefs,name,serviceArrayToSave,ftArrayToSave1,ftArrayToSave2,sqftArrayToSave,costArrayToSave,userDataAndTotalToSave)
        Log.e("OnCreate","${serviceArrayToSave} + ${ftArrayToSave1} + ${ftArrayToSave2} + ${sqftArrayToSave} + ${userDataAndTotalToSave}")
        val activity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.hide()


    }
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAllArrays()
        estimationViewModel = ViewModelProvider(this).get(EstimationViewModel::class.java)
        Log.e(
            "OnViewCreated",
            "${serviceArrayToSave} + ${ftArrayToSave1} + ${ftArrayToSave2} + ${sqftArrayToSave} + ${userDataAndTotalToSave} + "
        )
        binding.service1.onItemSelectedListener = this
        binding.service2.onItemSelectedListener = this
        binding.service3.onItemSelectedListener = this
        binding.service4.onItemSelectedListener = this
        binding.service5.onItemSelectedListener = this
        processRetrievedPrefsArray(
            serviceArrayToSave,
            ftArrayToSave1,
            ftArrayToSave2,
            sqftArrayToSave,
            costArrayToSave,
            userDataAndTotalToSave
        )
        binding.phone.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (binding.phone.text.toString().count() == 10) {
                    binding.phone.setText(processStringAfterEditing(binding.phone.text.toString()))
                }
            } else {
                if (binding.phone.text.toString().contains("(") && binding.phone.text.toString()
                        .contains(")") && binding.phone.text.toString().contains("-")
                ) {
                    binding.phone.setText(processStringBeforeEditing(binding.phone.text.toString()))
                }
            }
        }
        view.setOnClickListener {
            it.hideKeyboard()
        }
        binding.clear.setOnClickListener {
            it.hideKeyboard()
            clearAllData()
        }
        binding.calculate.setOnClickListener {
            calculate()
            it.hideKeyboard()
        }
        binding.save.setOnClickListener {
            processSave()
            clearAllData()
        }

    }


    override fun onPause() {
        super.onPause()

        savePrefs(prefs,name,serviceArray as ArrayList<Any>, ftArray1 as ArrayList<Any> ,
            ftArray2 as ArrayList<Any>, sqftArray as ArrayList<Any>,costArray as ArrayList<Any>,userDataAndTotalArray as ArrayList<Any> )


    }

    fun View.hideKeyboard()
    {
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken,0)
    }

    fun addAllArrays()
    {
        val serviceToAdd = listOf(binding.service1,binding.service2,binding.service3,binding.service4,binding.service5)
        serviceArray.addAll(serviceToAdd)
        val ftToAdd1 = listOf(binding.ft1,binding.ft12,binding.ft13,binding.ft14,binding.ft15)
        ftArray1.addAll(ftToAdd1)
        val ftToAdd2 = listOf(binding.ft2,binding.ft22,binding.ft23,binding.ft24,binding.ft25)
        ftArray2.addAll(ftToAdd2)
        val sqftToAdd = listOf(binding.sqft1,binding.sqft2,binding.sqft3,binding.sqft4,binding.sqft5)
        sqftArray.addAll(sqftToAdd)
        val costToAdd = listOf(binding.cost1,binding.cost2,binding.cost3,binding.cost4,binding.cost5)
        costArray.addAll(costToAdd)
        val userDataAndTotalToAdd = listOf(binding.name,binding.phone,binding.totalsqft,binding.totalcost)
        userDataAndTotalArray.addAll(userDataAndTotalToAdd)
    }

    fun savePrefs(prefs: SharedPreferences, name: ArrayList<String>, vararg input: ArrayList<Any>)
    {
        val editor = prefs.edit()
        for (index in 0..input.size-1)
        {
            editor.putInt("${name[index]}_size",input[index].count())
            if(input[index][0] is Spinner)
            {
                val arrayToSaveSpinner = input[index] as ArrayList<Spinner>
                for(i in 0..arrayToSaveSpinner.count()-1)
                {
                    editor.putInt("${name[index]}_${i}",arrayToSaveSpinner[i].selectedItemPosition)
                }
            }
            else
            {
                val arrayToSaveEditText = input[index] as ArrayList<EditText>
                for(i in 0..arrayToSaveEditText.count()-1)
                {
                    editor.putString("${name[index]}_${i}",arrayToSaveEditText[i].text.toString())
                }
            }
        }
        editor.apply()
    }

    fun retrievePrefs(prefs: SharedPreferences,name: ArrayList<String>, vararg array : ArrayList<Any>)
    {

        for (index in 0..array.size-1)
        {   array[index].clear()
            val count = prefs.getInt("${name[index]}_size",0)
            if(count != 0)
            {
                if (index == 0)
                {
                    for(i in 0..count-1)
                    {
                        val position = prefs.getInt("${name[index]}_${i}",0)
                        Log.d("RetrievePrefs","${index} + ${array[index]} + ${name[index]} + ${count} + ${position}")
                        array[index].add(position)
                    }
                }
                else
                {
                    for(i in 0..count-1)
                    {
                        val string = prefs.getString("${name[index]}_${i}","")
                        Log.d("RetrievePrefs2","${index} + ${array[index]} + ${name[index]} + ${count} + ${string}")
                        array[index].add(string.toString())
                    }
                }
            }
        }
    }

    fun processRetrievedPrefsArray(spinner: ArrayList<Any>,vararg array: ArrayList<Any>)
    {

        val field = arrayListOf(ftArray1,ftArray2,sqftArray,costArray,userDataAndTotalArray)
        for(i in 0..spinner.count()-1)
        {
            serviceArray[i].setSelection(spinner[i].toString().toInt())
        }
        for(index in 0..array.count()-1)
        {
            if(array[index].count()!=0)
            {

                for (i in 0..array[index].count()-1)
                {
                    Log.e("processRetrievedPrefs3","${index} + ${i} + ${field[index][i]} + ${array[index]} + ${array[index][i]}")
                    field[index][i].setText(array[index][i].toString())
                }
            }
        }
    }

    fun clearAllData()
    {
        for(i in 0..serviceArray.count()-1)
        {
            serviceArray[i].setSelection(0)
        }
        val field = arrayListOf(ftArray1,ftArray2,sqftArray,costArray,userDataAndTotalArray)
        for(index in 0..field.count()-1)
        {
            for (i in 0..field[index].count()-1)
            {
                field[index][i].setText("")
            }
        }
    }


    fun calculate()
    {

        var sqftTotalVal = 0.0
        var costTotalVal = 0.0
        for (i in 0..serviceArray.count()-1)
        {
            val decimal = DecimalFormat("#")
            if(ftArray1[i].text.toString()!="" && ftArray2[i].text.toString()!="")
            {
                val ft_1 = ftArray1[i].text.toString().toDouble()
                val ft_2 = ftArray2[i].text.toString().toDouble()
                var sqft = 0.0
                if(serviceArray[i].selectedItemPosition != 0 )
                {
                    when(serviceArray[i].selectedItemPosition)
                    {
                        1 -> sqft = (ft_1*ft_2)
                        2 -> sqft = (ft_1*ft_2)
                        3 -> sqft = (ft_1*ft_2)
                        12 -> sqft = (ft_1*ft_2)
                        13 -> sqft = (ft_1*ft_2)

                        else ->

                            sqft = (ft_1+ft_2)






                    }
                }
                sqftArray[i].setText(decimal.format(sqft))
                sqftTotalVal += sqft

                if(serviceArray[i].selectedItemPosition!=0)
                {   var cost = 0.0
                    when(serviceArray[i].selectedItemPosition)
                    {
                        1-> 	cost = round	((sqft)*(12))
                        2-> 	cost = round	((sqft)*(7))
                        3-> 	cost = round	((sqft)*(4.5))
                        4-> 	cost = round	((ft_1)*(28))
                        5-> 	cost = multipleOfFour(sqft) * 33.33
                        6-> 	cost = round	((sqft)*(31))
                        7-> 	cost = multipleOfSix(sqft) * 91.66
                        8-> 	cost = round 	((sqft)*(19))
                        9-> 	cost = multipleOfFour(sqft) * 33.33
                        10-> 	cost = round	((sqft)*(30))
                        11-> 	cost = multipleOfFour(sqft) * 100
                        12-> 	cost = multipleOfThree(sqft) * 30
                        13-> 	cost = multipleOfThree(sqft) * 26.6
                        14-> 	cost = round	((sqft)*(300))
                        15-> 	cost = round	((sqft)*(300))
                        16-> 	cost = round	((sqft)*(300))
                    }
                    costArray[i].setText(decimal.format(cost))
                    costTotalVal += cost
                }
            }
            binding.totalsqft.setText(decimal.format(sqftTotalVal))
            binding.totalcost.setText(decimal.format(costTotalVal))
        }
    }

    @InternalCoroutinesApi
    fun processSave() {
        if (binding.name.text.toString() == "" && binding.phone.text.toString() == "") {
            Toast.makeText(
                context,
                "Empty Phone and Name",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val serviceStringArray = serviceArray.asString(true)
            val ftStringArray1 = ftArray1.asString(false)
            val ftStringArray2 = ftArray2.asString(false)
            val sqftStringArray = sqftArray.asString(false)
            val costStringArray = costArray.asString(false)
            val userDataAndTotalStringArray = userDataAndTotalArray.asString(false)
            val estimation = Estimation(
                0,
                serviceStringArray,
                ftStringArray1,
                ftStringArray2,
                sqftStringArray,
                costStringArray,
                userDataAndTotalStringArray
            )
            Log.d("Save", "${serviceStringArray} +${ftStringArray1} +${ftStringArray2} + ${sqftStringArray} + ${costStringArray} + ${userDataAndTotalStringArray} +")
            estimationViewModel.insert(estimation)
            Toast.makeText(
                context,
                "Saved",
                Toast.LENGTH_LONG
            ).show()
            clearAllData()
        }
    }

    fun processStringAfterEditing(text:String):String
    {
        var substring = text.substring(0,3)
        val firstPart = "("+substring+")"
        substring = text.substring(3,6)
        val secondPart = substring+"-"
        val thirdPart = text.substring(6)
        return firstPart+secondPart+thirdPart
    }

    fun processStringBeforeEditing(text:String):String
    {
        var substring = text.substring(1,4)
        val firstPart = substring
        substring = text.substring(5,8)
        val secondPart = substring
        val thirdPart = text.substring(9)
        return firstPart+secondPart+thirdPart
    }

    fun <T> ArrayList<T>.asString(Spinner: Boolean) : ArrayList<String>
    {
        val array = arrayListOf<String>()
        if(Spinner)
        {
            val arrayToProcess = this as ArrayList<Spinner>
            arrayToProcess.map {
                array.add(it.selectedItemPosition.toString())

            }

        }
        else
        {
            val arrayToProcess = this as ArrayList<EditText>
            arrayToProcess.map {
                array.add(it.text.toString())

            }
        }
        return array
    }

    fun multipleOfFour(value: Double) : Double
    {
        var valueToReturn = value.roundToInt()
        if(valueToReturn%4 != 0)
        {
            if(valueToReturn < 4)
            {
                return 4.0
            }
            else
            {
                val quotient = (valueToReturn / 4 ) + 1
                valueToReturn = 4*quotient
                return valueToReturn.toDouble()
            }
        }
        return valueToReturn.toDouble()
    }

    fun multipleOfThree(value: Double) : Double
    {
        var valueToReturn = value.roundToInt()
        if(valueToReturn < 3)
        {
            return 3.0
        }
        return valueToReturn.toDouble()
    }

    fun multipleOfSix(value: Double) : Double
    {
        var valueToReturn = value.roundToInt()
        if(valueToReturn%6 != 0)
        {
            if(valueToReturn < 6)
            {
                return 6.0
            }
            else
            {
                val quotient = (valueToReturn / 6 ) + 1
                valueToReturn = 6*quotient
                Log.d("6","${quotient} + ${valueToReturn} + ${value}")
                return valueToReturn.toDouble()
            }
        }
        return valueToReturn.toDouble()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val parentSpinner = parent as Spinner
        Log.d("spinner","${parentSpinner}")
        if(parentSpinner == binding.service1)
        {
            if (parentSpinner.selectedItemPosition == 4 || position == 6 || position == 8 || position == 10) {

                binding.sqft1.setHint("Lft")
                binding.ft2.isFocusable = false
                binding.ft2.isCursorVisible = false
                binding.ft2.visibility = View.GONE
                binding.ft2.setText("0")

            }
            if (parentSpinner.selectedItemPosition == 5 || position == 7 || position == 9 || position == 11) {

                binding.sqft1.setHint("Gate")
                binding.ft2.isFocusable = false
                binding.ft2.isCursorVisible = false
                binding.ft2.visibility = View.GONE
                binding.ft2.setText("0")
            }

            else  {
                binding.ft2.visibility = View.VISIBLE
                binding.ft2.isFocusable = true
                binding.ft2.isCursorVisible = true
                binding.ft2.setText("")
                binding.sqft1.setHint("Sqft")

            }


        }
        else if(parentSpinner == binding.service2)
        {
            if (parentSpinner.selectedItemPosition == 4) {

                binding.sqft2.setHint("Lft")
                binding.ft22.isFocusable = false
                binding.ft22.isCursorVisible = false
                binding.ft22.visibility = View.GONE
            }
                else  {
                binding.ft22.visibility = View.VISIBLE
                binding.ft22.isFocusable = true
                binding.ft22.isCursorVisible = true

            }
        }
        else if(parentSpinner == binding.service3)
        {
            if (parentSpinner.selectedItemPosition == 4) {

                binding.sqft3.setHint("Lft")
                binding.ft23.isFocusable = false
                binding.ft23.isCursorVisible = false
                binding.ft23.visibility = View.GONE
            }
            else  {
                binding.ft23.visibility = View.VISIBLE
                binding.ft23.isFocusable = true
                binding.ft23.isCursorVisible = true

            }

        }
        else if(parentSpinner == binding.service4)
        {
            if (parentSpinner.selectedItemPosition == 4) {

                binding.sqft4.setHint("Lft")
                binding.ft24.isFocusable = false
                binding.ft24.isCursorVisible = false
                binding.ft24.visibility = View.GONE
            }
            else  {
                binding.ft24.visibility = View.VISIBLE
                binding.ft24.isFocusable = true
                binding.ft24.isCursorVisible = true

            }

        }
        else if(parentSpinner == binding.service5)
        {
            if (parentSpinner.selectedItemPosition == 4) {

                binding.sqft5.setHint("Lft")
                binding.ft25.isFocusable = false
                binding.ft25.isCursorVisible = false
                binding.ft25.visibility = View.GONE
            }
            else  {
                binding.ft25.visibility = View.VISIBLE
                binding.ft25.isFocusable = true
                binding.ft25.isCursorVisible = true

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
