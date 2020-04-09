package com.jfcgraphicsllc.landscaping

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EstimationDao
{
    @Query("SELECT * from estimation_table")
    fun getOrderedEstimation(): LiveData<List<Estimation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estimation:Estimation)

    @Update
    suspend fun update(estimation: Estimation)

    @Delete
    suspend fun remove(estimation: Estimation)
}
