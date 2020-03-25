package com.example.landscaping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class EstimationViewModel(application: Application):AndroidViewModel(application)
{
    private val repository: EstimationRepository

    val allEstimation: LiveData<List<Estimation>>

    init {
        val estimationDao = EstimationDatabase.getDatabase(application).estimationDao()
        repository = EstimationRepository(estimationDao)
        allEstimation = repository.allEstimation
    }

    fun insert(estimation: Estimation) = viewModelScope.launch {
        repository.insert(estimation)
    }
}