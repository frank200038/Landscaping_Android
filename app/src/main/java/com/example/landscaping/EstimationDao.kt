package com.example.landscaping

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EstimationDao
{
    @Query("SELECT * from estimation_table ORDER BY phone ASC")
    fun getOrderedEstimation(): LiveData<ArrayList<Estimation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estimation:Estimation)
}
