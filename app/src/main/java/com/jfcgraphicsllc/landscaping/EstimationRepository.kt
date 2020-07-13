package com.jfcgraphicsllc.landscaping

import androidx.lifecycle.LiveData

class EstimationRepository(private val estimationDao:EstimationDao)
{
    val allEstimation: LiveData<List<Estimation>> = estimationDao.getOrderedEstimation()
    suspend fun insert(estimation:Estimation)
    {
        estimationDao.insert(estimation)
    }

    suspend fun update(estimation: Estimation) {
        estimationDao.update(estimation)
    }

    suspend fun remove(estimation: Estimation)
    {
        estimationDao.remove(estimation)
    }
}
