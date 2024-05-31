package com.example.bmicalculator.repository

import androidx.annotation.WorkerThread
import com.example.bmicalculator.model.dao.BmiDAO
import com.example.bmicalculator.model.dto.Bmi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BmiRepository(private val bmiDAO: BmiDAO) {
    val getAllBmi : Flow<List<Bmi>> = bmiDAO.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getBmi(bmi: Bmi) {
        withContext(Dispatchers.IO) {
            bmiDAO.getBmi(bmi.id)
        }

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(bmi: Bmi) {
        withContext(Dispatchers.IO) {
            bmiDAO.insert(bmi)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(bmi: Bmi) {
        withContext(Dispatchers.IO) {
            bmiDAO.update(bmi)
        }

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(bmi: Bmi) {
        withContext(Dispatchers.IO) {
            bmiDAO.delete(bmi)
        }

    }
}