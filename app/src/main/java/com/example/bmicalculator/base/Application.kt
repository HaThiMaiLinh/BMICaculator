package com.example.bmicalculator.base

import android.app.Application
import com.example.bmicalculator.repository.BmiRepository
import com.example.bmicalculator.room.AppDatabase
import com.example.bmicalculator.utils.SharePreferencesUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class Application : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BmiRepository(database.bmiDAO()) }
    override fun onCreate() {
        super.onCreate()
        SharePreferencesUtils.init(this)
    }
}