package com.example.landscaping

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = arrayOf(Estimation::class), version = 1, exportSchema = false)
public abstract class EstimationDatabase: RoomDatabase()
{
    abstract fun estimationDao():EstimationDao

    companion object
    {
        @Volatile
        private  var INSTANCE: EstimationDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context):EstimationDatabase
        {
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext,EstimationDatabase::class.java,"estimation_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
