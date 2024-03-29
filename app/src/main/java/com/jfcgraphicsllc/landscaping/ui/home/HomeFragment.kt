package com.jfcgraphicsllc.landscaping.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jfcgraphicsllc.landscaping.Estimation
import com.jfcgraphicsllc.landscaping.EstimationViewModel
import com.jfcgraphicsllc.landscaping.R
import com.jfcgraphicsllc.landscaping.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import kotlin.collections.ArrayList


import kotlin.math.round
import kotlin.math.roundToInt

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var serviceArray: ArrayList<Spinner> = arrayListOf()
    private var ftArray1: ArrayList<EditText> = arrayListOf()
    private var ftArray2: ArrayList<EditText> = arrayListOf()
    private var sqftArray: ArrayList<EditText> = arrayListOf()
    private var costArray: ArrayList<EditText> = arrayListOf()
    private var userDataAndTotalArray: ArrayList<EditText> = arrayListOf()
    private lateinit var prefs: SharedPreferences
    private lateinit var prefs2 : SharedPreferences
    private var serviceArrayToSave: ArrayList<Any> = arrayListOf()
    private var ftArrayToSave1: ArrayList<Any> = arrayListOf()
    private var ftArrayToSave2: ArrayList<Any> = arrayListOf()
    private var sqftArrayToSave: ArrayList<Any> = arrayListOf()
    private var costArrayToSave: ArrayList<Any> = arrayListOf()
    private var userDataAndTotalToSave: ArrayList<Any> = arrayListOf()
    private var estimation: Estimation? = null
    private val crashlytics : FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
    private val name = arrayListOf("Service", "ft1", "ft2", "sqft", "cost", "userData")

    @InternalCoroutinesApi
    private lateinit var estimationViewModel: EstimationViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        prefs2 = context.getSharedPreferences("Database_info",Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(crashlytics.didCrashOnPreviousExecution())
        {
            Log.e("Wrong","wrong")
            prefs.edit().clear().commit()

        }
        if(arguments?.getSerializable("data") != null) {
            estimation = arguments?.getSerializable("data") as Estimation
            //Retrieve Estimation Data when user edits the entry
            retrieveDataFromHistory(
                estimation!!,
                serviceArrayToSave,
                ftArrayToSave1,
                ftArrayToSave2,
                sqftArrayToSave,
                costArrayToSave,
                userDataAndTotalToSave)
        }
        else {
            //Normal situation, retrieves data from SharedPrefereneces (Fragment destroy, or app closed)
            retrievePrefs(
                prefs,
                name,
                serviceArrayToSave,
                ftArrayToSave1,
                ftArrayToSave2,
                sqftArrayToSave,
                costArrayToSave,
                userDataAndTotalToSave
            )
        }
        Log.e(
            "OnCreate",
            "${serviceArrayToSave} + ${ftArrayToSave1} + ${ftArrayToSave2} + ${sqftArrayToSave} + ${userDataAndTotalToSave}"
        )
        val activity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.hide()


    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAllArrays()
        changeScrollViewHeight()
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
        //Auto format of phone number into US Phone Format (xxx)xxx-xxxx if applicable
        binding.phone.setOnFocusChangeListener { _v, hasFocus ->
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
            if (estimation != null) {
                processSave(true)

            } else {
                processSave(false)
                clearAllData()
            }
        }
        if(estimation == null) {
            binding.logout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
            }
        }
        else
        {
            binding.logout.visibility = View.INVISIBLE
            binding.logouttext.visibility = View.INVISIBLE
        }
    }


    override fun onPause() {
        super.onPause()
        //Save the preferences when Fragment is on Pause and only if the fragment is not created from editing
        if(estimation == null) {
            savePrefs(
                prefs,
                name,
                serviceArray as ArrayList<Any>,
                ftArray1 as ArrayList<Any>,
                ftArray2 as ArrayList<Any>,
                sqftArray as ArrayList<Any>,
                costArray as ArrayList<Any>,
                userDataAndTotalArray as ArrayList<Any>
            )
        }

    }

    fun View.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun addAllArrays() {
        val serviceToAdd = arrayListOf(
            binding.service1,
            binding.service2,
            binding.service3,
            binding.service4,
            binding.service5
        )
        serviceArray = serviceToAdd
        val ftToAdd1 = arrayListOf(binding.ft1, binding.ft12, binding.ft13, binding.ft14, binding.ft15)
        ftArray1 = ftToAdd1
        val ftToAdd2 = arrayListOf(binding.ft2, binding.ft22, binding.ft23, binding.ft24, binding.ft25)
        ftArray2 = ftToAdd2
        val sqftToAdd =
            arrayListOf(binding.sqft1, binding.sqft2, binding.sqft3, binding.sqft4, binding.sqft5)
        sqftArray = sqftToAdd
        val costToAdd =
            arrayListOf(binding.cost1, binding.cost2, binding.cost3, binding.cost4, binding.cost5)
        costArray = costToAdd
        val userDataAndTotalToAdd =
            arrayListOf(binding.name, binding.phone, binding.totalsqft, binding.totalcost)
        userDataAndTotalArray = userDataAndTotalToAdd
    }

    //Save Preferences
    fun savePrefs(prefs: SharedPreferences, name: ArrayList<String>, vararg input: ArrayList<Any>) {
        val editor = prefs.edit()
        for (index in input.indices) {
            editor.putInt("${name[index]}_size", input[index].count()) //Register the count of the array using the name of the array
            if (input[index][0] is Spinner) { // If the first item is Spinner, then array is Spinner
                val arrayToSaveSpinner = input[index] as ArrayList<Spinner>
                for (i in arrayToSaveSpinner.indices) {
                    editor.putInt("${name[index]}_${i}", arrayToSaveSpinner[i].selectedItemPosition) // Save the Item Position of the Spinner to SharedPreferences
                }
            } else { //If array is EditText
                val arrayToSaveEditText = input[index] as ArrayList<EditText>
                for (i in arrayToSaveEditText.indices) {
                    editor.putString("${name[index]}_${i}", arrayToSaveEditText[i].text.toString()) //Save text to SharedPreferences
                }
            }
        }
        editor.apply()
    }

    fun retrievePrefs(
        prefs: SharedPreferences,
        name: ArrayList<String>,
        vararg array: ArrayList<Any>
    ) {

        for (index in 0..array.size - 1) {
            array[index].clear() //Clear array that is used to store the data
            val count = prefs.getInt("${name[index]}_size", 0) // Retrieve the size of the array by searching the name
            if(count == 5 || count == 4) { // If the array size is either 5 or 4 (Means normal situation)
                if (index == 0) { // The first is always Service Array (Spinner)
                    for (i in 0..count - 1) {
                        val position = prefs.getInt("${name[index]}_${i}", 0) //Get the position that suppose to be assigned to Spinner
                        Log.d(
                            "RetrievePrefs",
                            "${index} + ${array[index]} + ${name[index]} + ${count} + ${position}"
                        )
                        array[index].add(position)
                    }
                } else { //The rest is always EditText array
                    for (i in 0..count - 1) {
                        val string = prefs.getString("${name[index]}_${i}", "") // Retrieve the text
                        Log.d(
                            "RetrievePrefs2",
                            "${index} + ${array[index]} + ${name[index]} + ${count} + ${string}"
                        )
                        array[index].add(string.toString())
                    }
                }
            }
            else if (count != 0) //If the array size is not 5 or 4 means there is problem in saving. Handle Exception by clearning the sharedPreferences (No need to operate if array is empty)
            {
                prefs.edit().clear().commit()
                break
            }
        }
    }

    //Process Estimation data if user tries to edit it
    fun retrieveDataFromHistory(estimation:Estimation,vararg array: ArrayList<Any>)
    {
        val count = arrayListOf(estimation.service.count(),estimation.ft1.count(),estimation.ft2.count(),estimation.sqft.count(),estimation.cost.count(),estimation.userDataAndTotal.count())
        val data = arrayListOf(estimation.service,estimation.ft1,estimation.ft2,estimation.sqft,estimation.cost,estimation.userDataAndTotal)
        for (index in 0..array.size-1)
        {
            array[index].clear()
            if(count[index] != 0)
            {
                if (index == 0) //First index is always Service array
                {
                    for(i in 0 until count[index])
                    {
                        val service = data[index][i]
                        array[index].add(service.toInt()) //Add Int type of Serivce position to the array
                        Log.d("RetrieveData","${index} + ${array[index]} + ${count}+${service}")

                    }
                }
                else
                {
                    for(i in 0 until count[index])
                    {
                        val dataToSave = data[index][i]
                        Log.d("RetrieveData2","${index} + ${array[index]} + ${count} + ${dataToSave}")
                        array[index].add(dataToSave) //Add text to the array
                    }
                }
            }
        }
    }

    //Apply text and selected position on theirs correspondent EditText and Spinners
    fun processRetrievedPrefsArray(spinner: ArrayList<Any>, vararg array: ArrayList<Any>) {

        val field = arrayListOf(ftArray1, ftArray2, sqftArray, costArray, userDataAndTotalArray)
        //Process Spinner data, has to be seperated
        for (i in spinner.indices) {
            serviceArray[i].setSelection(spinner[i].toString().toInt())
        }
        //Process String to apply on EditText
        for (index in array.indices) {
            if (array[index].count() != 0) {
                for (i in array[index].indices) {
                    field[index][i].setText(array[index][i].toString())
                    Log.e(
                        "processRetrievedPrefs3",
                        "${index} + ${i} + ${field[index][i]} + ${array[index]} + ${array[index][i]} + ${field[index][i].text}")
                }
            }
        }
    }

    fun clearAllData() {
        for (i in 0..serviceArray.count() - 1) {
            serviceArray[i].setSelection(0)
        }
        val field = arrayListOf(ftArray1, ftArray2, sqftArray, costArray, userDataAndTotalArray)
        for (index in 0..field.count() - 1) {
            for (i in 0..field[index].count() - 1) {
                field[index][i].setText("")
            }
        }
    }


    fun calculate() {
        var sqftTotalVal = 0.0
        var costTotalVal = 0.0
        val decimal = DecimalFormat("#")
        val worksWithEmpty = arrayListOf(4, 5, 6, 7, 8, 9, 10, 11, 16)
        for (i in 0..serviceArray.count() - 1)
        {
            val position = serviceArray[i].selectedItemPosition
            var sqft: Double
            if (position != 0) {
                Log.d("Contains","${worksWithEmpty.contains(position)}")
                if (worksWithEmpty.contains(position))  // These are serives that don't need to have both ft1 and ft2 entered
                {
                    val ft1 = if (ftArray1[i].text.toString() != "") ftArray1[i].text.toString() //Assign automatically 0.0 to ft1 is not entered
                        .toDouble() else 0.0
                    val ft2 = if (ftArray2[i].text.toString() != "") ftArray2[i].text.toString() //Assign automatically 0.0 to ft2 is not entered
                        .toDouble() else 0.0
                    sqft = ft1 + ft2
                    Log.d("Empty","${ft1} + ${ft2} + ${sqft}")
                    Log.d("Sqft", "${sqft}")
                    sqftArray[i].setText(decimal.format(sqft))
                    sqftTotalVal += sqft
                    var cost  = 0.0
                    when(position)
                    {
                        4 -> cost = round((sqft) * (28))
                        5 -> cost = multipleOfFour(sqft) * 33.33
                        6 -> cost = round((sqft) * (31))
                        7 -> cost = multipleOfSix(sqft) * 91.66
                        8 -> cost = round((sqft) * (19))
                        9 -> cost = multipleOfFour(sqft) * 33.33
                        10 -> cost = round((sqft) * (30))
                        11 -> cost = multipleOfFour(sqft) * 100
                        16 -> cost = round((sqft) * (300))
                    }
                    costArray[i].setText(decimal.format(cost))
                    costTotalVal += cost
                }
                else //Services that needs to have a value of ft1 and ft2
                {
                    if (ftArray1[i].text.toString() != "" && ftArray2[i].text.toString() != "")
                    {
                        val ft1 = ftArray1[i].text.toString().toDouble()
                        val ft2 = ftArray2[i].text.toString().toDouble()
                        sqft = ft1 * ft2
                        Log.d("NoEmpty","${ft1} + ${ft2} + ${sqft}")
                        Log.d("Sqft", "${sqft}")
                        sqftArray[i].setText(decimal.format(sqft))
                        sqftTotalVal += sqft
                        var cost  = 0.0
                        when(position)
                        {
                            1 -> cost = round((sqft) * (12))
                            2 -> cost = round((sqft) * (7))
                            3 -> cost = round((sqft) * (4.5))
                            12 -> cost = mulchPrice(sqft)
                            13 -> cost = mulchPriceNatural(sqft)
                            14 -> cost = round((sqft) * (300))
                            15 -> cost = round((sqft) * (300))
                        }
                        costArray[i].setText(decimal.format(cost))
                        costTotalVal += cost
                    }
                    else
                    {
                        sqftArray[i].setText("") //If either ft1 or ft2 is empty, set the sqftText to ""
                        costArray[i].setText("") //If either ft1 or ft2 is empty, set the costText to ""
                    }
                }
            }
        }
        binding.totalsqft.setText(decimal.format(sqftTotalVal))
        binding.totalcost.setText(decimal.format(costTotalVal))
    }


    @InternalCoroutinesApi
    fun processSave(update: Boolean) {
        if (binding.name.text.toString() == "" || binding.phone.text.toString() == "")
        {
            Toast.makeText(
                context,
                "Empty Phone and Name",
                Toast.LENGTH_LONG
            ).show()
        }
        else
        {
            val serviceStringArray = serviceArray.asString(true)
            val ftStringArray1 = ftArray1.asString(false)
            val ftStringArray2 = ftArray2.asString(false)
            val sqftStringArray = sqftArray.asString(false)
            val costStringArray = costArray.asString(false)
            val userDataAndTotalStringArray = userDataAndTotalArray.asString(false)
            if(update) //If the user is trying to save an edited estimation
            {
                val estimation = Estimation(
                    this.estimation!!.id,
                    serviceStringArray,
                    ftStringArray1,
                    ftStringArray2,
                    sqftStringArray,
                    costStringArray,
                    userDataAndTotalStringArray
                )
                estimationViewModel.update(estimation)
                jsonConverterAndSave(estimation,true) //Convert saved estimation to JSON. Prepare to save to FireStore
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Entry Edited")
                alertDialog.setMessage("You entry has been sucessfully edited")
                alertDialog.setNegativeButton("Exit"){dialog, which ->
                    activity?.onBackPressed()
                }
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            else
            { //Save estimation as a new one
                val id = prefs2.getInt("database_id",1)
                val estimation = Estimation(
                    id,
                    serviceStringArray,
                    ftStringArray1,
                    ftStringArray2,
                    sqftStringArray,
                    costStringArray,
                    userDataAndTotalStringArray
                )
                prefs2.edit().putInt("database_id",id+1).commit()
                estimationViewModel.insert(estimation)
                jsonConverterAndSave(estimation,false) //Convert data to JSON, prepare for upload to firestore
                Log.e("Save","${estimation}")
            }
            Toast.makeText(
                context,
                "Saved",
                Toast.LENGTH_LONG
            ).show()
            GlobalScope.launch {
                uploadToFireStore() //Upload to firestore
            }
        }
    }

    fun processStringAfterEditing(text:String):String
    {
        var substring = text.substring(0,3)
        val firstPart = "($substring)"
        substring = text.substring(3,6)
        val secondPart = "$substring-"
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

    //Extension of ArrayList function, convert directly an array to ArrayList String with contents
    fun <T> ArrayList<T>.asString(Spinner: Boolean) : ArrayList<String>
    {
        val array = arrayListOf<String>()
        if(Spinner) //Convert Serivce positon to string and save in Array
        {
            val arrayToProcess = this as ArrayList<Spinner>
            arrayToProcess.map {
                array.add(it.selectedItemPosition.toString())

            }

        }
        else //Saved text to array
        {
            val arrayToProcess = this as ArrayList<EditText>
            arrayToProcess.map {
                array.add(it.text.toString())

            }
        }
        return array
    }

    fun read():String //Read the json file
    {
        val file = requireContext().openFileInput("estimation.json")
        file.use{
                stream -> val text = stream.bufferedReader().use{
            it.readText()
        }
            return text
        }
    }

    fun create(json: String):Boolean //If file doesn't create, create a new one
    {
        val filename = "estimation.json"
        return try {
            requireContext().openFileOutput(filename,Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }

    }

    fun isFilePresent() : Boolean //Check if file is present
    {
        val path = requireContext().filesDir.absolutePath + "/estimation.json"
        val file = File(path)
        return file.exists()
    }

    fun jsonConverterAndSave(estimation: Estimation,edit: Boolean)
    {
        if(isFilePresent()) //If file is present
        {
            val string = read() //Read from the existint file
            val listType = object: TypeToken<MutableList<Estimation>>() {}.type
            val estimationFromJson : MutableList<Estimation> = Gson().fromJson(string,listType) //use Gson to convert JSON to <Estimation>
           // Log.e("Json1","${string} + ${estimationFromJson} + ${estimation}")
            if(edit) //If it is an edition of existing entry
            {
                for(i in 0 until estimationFromJson.count())
                {
                    if(estimationFromJson[i].id == estimation.id) //Find the estimation in the json file
                    {
                        estimationFromJson[i] = estimation //Replace the old one with the current one
                    }
                }
            }
            else
            {
                estimationFromJson.add(estimation) // It is a new estimation, add directly
            }
            val estimataionToJson = Gson().toJson(estimationFromJson,listType) //Convert Estimation back to Json format
            create(estimataionToJson)  // Create a new version of the Json file
           // Log.e("Json","${estimationFromJson}")
        }
        else //If file doesn't exist
        {
            val estimationToAdd = listOf(estimation) //Add this estimation as the start of the new json file
            val listType = object: TypeToken<List<Estimation>>() {}.type
            val estimataionToJson = Gson().toJson(estimationToAdd,listType) //Convert to json file
           // Log.e("Json3","${estimationToAdd}")
            create(estimataionToJson) //Create this json file
        }
    }

    fun uploadToFireStore()
    {
        val storage = Firebase.storage
        val user = FirebaseAuth.getInstance().currentUser
        val jsonRef = storage.reference.child("users/${user!!.uid}/estimation.json")
        val pathForFile = requireContext().filesDir.absolutePath + "/estimation.json"
        Log.e("User id",user.uid)
        val file = Uri.fromFile(File(pathForFile))
        val uploadTask = jsonRef.putFile(file)
        uploadTask.addOnSuccessListener {
            Log.e("Write Success","Sucess")
        }.addOnFailureListener{
            Log.e("Write wrong","${it}")
        }
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

    fun mulchPrice(sqft: Double) : Double
    {
        if (sqft <= 3)
        {
            return 450.00
        }
        else
        {
            return sqft * 90
        }

    }

    fun mulchPriceNatural(sqft: Double) : Double
    {
        if (sqft <= 5)
        {
            return 700.00
        }
        else
        {
            return sqft * 80
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val parentSpinner = parent as Spinner
        Log.d("spinner","${parentSpinner}")
        if(parentSpinner == binding.service1)
        {
            if (position == 4 || position == 6 || position == 8 || position == 10 ) {

                binding.sqft1.hint = "Lft"
                binding.ft2.visibility = View.GONE

                Log.d("spinner","${position}")
            }
            else if (position == 5 || position == 7 || position == 9 || position == 11) {
                binding.ft2.visibility = View.GONE
                binding.sqft1.hint = "Gate"
                binding.ft1.hint = "Ft"
                Log.d("spinner", "${position}")
            }
            else if (position == 16) {
                binding.ft2.visibility = View.GONE
                binding.ft1.hint = "Hrs"
                binding.sqft1.hint = "Hrs"
                Log.d("spinner","${position}")
            }
            else {
                binding.ft2.visibility = View.VISIBLE
                binding.ft2.isFocusable = true
                binding.ft2.isCursorVisible = true
                binding.ft1.hint = "Ft"
                binding.sqft1.hint = "Sqft"
                Log.d("spinner","${position}")
            }
        }
        else if(parentSpinner == binding.service2) {
            if (position == 4 || position == 6 || position == 8 || position == 10) {

                binding.sqft2.hint = "Lft"
                binding.ft22.visibility = View.GONE
                Log.d("spinner", "${position}")
            } else if (position == 5 || position == 7 || position == 9 || position == 11) {
                binding.ft22.visibility = View.GONE
                binding.sqft2.hint = "Gate"
                binding.ft12.hint = "Ft"
                Log.d("spinner", "${position}")
            } else if (position == 16) {
                binding.ft22.visibility = View.GONE
                binding.ft12.hint = "Hrs"
                binding.sqft2.hint = "Hrs"
                Log.d("spinner", "${position}")
            } else {
                binding.ft22.visibility = View.VISIBLE
                binding.ft22.isFocusable = true
                binding.ft22.isCursorVisible = true
                binding.ft12.hint = "Ft"
                binding.sqft2.hint = "Sqft"
                Log.d("spinner", "${position}")
            }
        }
        else if(parentSpinner == binding.service3)
        {
            if (position == 4 || position == 6 || position == 8 || position == 10 ) {
                binding.sqft3.hint = "Lft"
                binding.ft23.visibility = View.GONE
                Log.d("spinner","${position}")
            }
            else if (position == 5 || position == 7 || position == 9 || position == 11) {
                binding.ft23.visibility = View.GONE
                binding.sqft3.hint = "Gate"
                binding.ft13.hint = "Ft"
                Log.d("spinner", "${position}")
            }
            else if (position == 16) {
                binding.ft23.visibility = View.GONE
                binding.ft13.hint= "Hrs"
                binding.sqft3.hint= "Hrs"
                Log.d("spinner","${position}")
            }
            else  {
                binding.ft23.visibility = View.VISIBLE
                binding.ft23.isFocusable = true
                binding.ft23.isCursorVisible = true
                binding.ft13.hint = "Ft"
                binding.sqft3.hint = "Sqft"
                Log.d("spinner","${position}")
            }

        }
        else if(parentSpinner == binding.service4)
        {
            if (position == 4 || position == 6 || position == 8 || position == 10 ) {

                binding.sqft4.hint = "Lft"
                binding.ft24.visibility = View.GONE

                Log.d("spinner","${position}")
            }
            else if (position == 5 || position == 7 || position == 9 || position == 11) {
                binding.ft24.visibility = View.GONE
                binding.sqft4.hint = "Gate"
                binding.ft14.hint = "Ft"
                Log.d("spinner", "${position}")
            }
            else if (position == 16) {
                binding.ft24.visibility = View.GONE
                binding.ft14.hint= "Hrs"
                binding.sqft4.hint= "Hrs"
                Log.d("spinner","${position}")
            }
            else  {
                binding.ft24.visibility = View.VISIBLE
                binding.ft24.isFocusable = true
                binding.ft24.isCursorVisible = true
                binding.ft14.hint = "Ft"
                binding.sqft4.hint = "Sqft"
                Log.d("spinner","${position}")
            }
        }

        else if(parentSpinner == binding.service5)
        {
            if (position == 4 || position == 6 || position == 8 || position == 10 ) {

                binding.sqft5.hint = "Lft"
                binding.ft25.visibility = View.GONE

                Log.d("spinner","${position}")
            }
            else if (position == 5 || position == 7 || position == 9 || position == 11) {
                binding.ft25.visibility = View.GONE
                binding.sqft5.hint = "Gate"
                binding.ft15.hint = "Ft"
                Log.d("spinner", "${position}")
            }
            else if (position == 16) {
                binding.ft25.visibility = View.GONE
                binding.ft15.hint= "Hrs"
                binding.sqft5.hint= "Hrs"
                Log.d("spinner","${position}")
            }
            else  {
                binding.ft25.visibility = View.VISIBLE
                binding.ft25.isFocusable = true
                binding.ft25.isCursorVisible = true
                binding.ft15.hint = "Ft"
                binding.sqft5.hint = "Sqft"
                Log.d("spinner","${position}")
            }
        }
    }

    fun changeScrollViewHeight()
    {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,400f,resources.displayMetrics)
        binding.scrollView2.layoutParams.height = height - pixels.toInt()

        Log.d("View","${height} + ${pixels} + ${binding.scrollView2.height} + ${binding.scrollView2.layoutParams.height}")
    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")

    }
}
