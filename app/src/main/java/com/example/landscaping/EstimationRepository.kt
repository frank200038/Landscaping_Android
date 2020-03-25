package com.example.landscaping

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

class EstimationRepository(private val estimationDao:EstimationDao)
{
    val allEstimation: LiveData<ArrayList<Estimation>> = estimationDao.getOrderedEstimation()
    suspend fun insert(estimation:Estimation)
    {
        estimationDao.insert(estimation)
    }
}
