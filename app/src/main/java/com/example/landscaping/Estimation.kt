package com.example.landscaping

import android.widget.Spinner
import androidx.annotation.*
import androidx.room.*


@Entity(tableName = "estimation_table")
data class Estimation(var names:String,var phones:String, var services:ArrayList<Int>,var ft1s: ArrayList<String>,var ft2s: ArrayList<String>,var sqfts: ArrayList<String>,var costs: ArrayList<String>,var sqftTotals: ArrayList<String>,var costTotals: ArrayList<String>)
{
    @PrimaryKey(autoGenerate = true) private id:int
    @ColumnInfo(name = "name") private val name: String
    @ColumnInfo(name = "phone") private val phone: String
    @ColumnInfo(name = "service") private  val service: ArrayList<Int>
    @ColumnInfo(name = "ft1") private val ft1: ArrayList<String>
    @ColumnInfo(name = "ft2") private val ft2: ArrayList<String>
    @ColumnInfo(name = "sqft") private val sqft: ArrayList<String>
    @ColumnInfo(name = "cost") private val cost: ArrayList<String>
    @ColumnInfo(name = "sqftTotal") private val sqftTotal: ArrayList<String>
    @ColumnInfo(name = "costTotal") private val costTotal: ArrayList<String>

    init
    {
        this.name = names
        this.phone = phones
        this.service = services
        this.ft1 = ft1s
        this.ft2 = ft2s
        this.sqft = sqfts
        this.cost = costs
        this.sqftTotal = sqftTotals
        this.costTotal = costTotals
    }

    fun getName():String
    {
        return name
    }

    fun getPhone():String
    {
        return phone
    }

    fun getService():ArrayList<Int>
    {
        return service
    }

    fun getFt1():ArrayList<String>
    {
        return ft1
    }

    fun getFt2():ArrayList<String>
    {
        return ft2
    }

    fun getSqFT():ArrayList<String>
    {
        return sqft
    }

    fun getSqFtTotal():ArrayList<String>
    {
        return sqftTotal
    }

    fun getCostTotal():ArrayList<String>
    {
        return costTotal
    }

    fun setId(id:Int)
    {
        this.id = id
    }

}


