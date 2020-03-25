package com.example.landscaping

import android.widget.Spinner
import androidx.annotation.*
import androidx.room.*


@Entity(tableName = "estimation_table")
data class Estimation(//var names:String,var phones:String, var services:ArrayList<String>,var ft1s: ArrayList<String>,var ft2s: ArrayList<String>,var sqfts: ArrayList<String>,var costs: ArrayList<String>,var sqftTotals: String,var costTotals: String)

    @PrimaryKey(autoGenerate = true)  var id:Int = 0,
    @ColumnInfo(name = "name")  var name: String = "",
    @ColumnInfo(name = "phone")  var phone: String = "",
    @ColumnInfo(name = "service")   var service: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "ft1")  var ft1: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "ft2")  var ft2: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "sqft")  var sqft: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "cost")  var cost: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "sqftTotal")  var sqftTotal: String = "",
    @ColumnInfo(name = "costTotal")  var costTotal: String = ""
)
{
   constructor() : this(0,"","",ArrayList<String>(5),ArrayList<String>(5),ArrayList<String>(5),ArrayList<String>(5),ArrayList<String>(5),"","")

//    fun getName():String
//    {
//        return name
//    }
//
//    fun getPhone():String
//    {
//        return phone
//    }
//
//    fun getService():ArrayList<Int>
//    {
//        return service
//    }
//
//    fun getFt1():ArrayList<String>
//    {
//        return ft1
//    }
//
//    fun getFt2():ArrayList<String>
//    {
//        return ft2
//    }
//
//    fun getSqFT():ArrayList<String>
//    {
//        return sqft
//    }
//
//    fun getSqFtTotal():ArrayList<String>
//    {
//        return sqftTotal
//    }
//
//    fun getCostTotal():ArrayList<String>
//    {
//        return costTotal
//    }


}


