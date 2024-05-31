package com.example.bmicalculator.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bmicalculator.model.dto.Bmi
import com.example.bmicalculator.repository.BmiRepository
import kotlinx.coroutines.launch

class BmiViewModel(private val repository: BmiRepository) :ViewModel() {

    val allBmi: LiveData<List<Bmi>> = repository.getAllBmi.asLiveData()

    fun getBmi(bmi: Bmi) = viewModelScope.launch {
        repository.getBmi(bmi)
    }

    fun insert(bmi: Bmi) = viewModelScope.launch {
        repository.insert(bmi)
    }

    fun update(bmi: Bmi) = viewModelScope.launch {
        repository.update(bmi)
    }

    fun delete(bmi: Bmi) = viewModelScope.launch {
        repository.delete(bmi)
    }
}

class BmiViewModelFactory(private val repository: BmiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BmiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BmiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}